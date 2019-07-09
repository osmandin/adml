package edu.mit.adml.controller;

import edu.mit.adml.DatabaseInitializer;
import edu.mit.adml.domain.Item;
import edu.mit.adml.repository.ItemRepository;
import edu.mit.adml.service.ItemService;
import edu.mit.adml.util.Util;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class AddController {

    public static final String ACCESS = "access";
    private final Logger logger = getLogger(this.getClass());

    //private final String server = "159.203.105.249"; //"emmastaff-lib.mit.edu";//"159.203.105.249";

    private final String server = "emmastaff-lib.mit.edu";
    private final int port = 8089;

    private ItemService itemService;

    @Autowired
    public AddController(ItemService studentService) {
        this.itemService = studentService;
    }

    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Model greetingForm(final Model model, HttpServletRequest httpServletRequest) {

        final Item item = new Item();

        // Page<Item> items = itemService.findByItemId(id, new PageRequest(0, 1));

        // TODO: decide where things like this go:

        final Map<Integer, String> formats = Util.getFormats();
        model.addAttribute("format", formats);

        final Map<Integer, String> transferStatus = Util.getTransferStatus();
        //transferStatus.put(1, "Transferred");
        model.addAttribute("transferStatus", transferStatus);

        final Map<Integer, String> transferMethods = Util.getTransferMethods();
        //transferMethods.put(1, "Upload");
        model.addAttribute("transferMethod", transferMethods);

        final Map<Integer, String> itemDisposition = Util.getDispositions();
        //itemDisposition.put(1, "Disposed");
        model.addAttribute("disposition", itemDisposition);

        model.addAttribute("item", item);

        logger.info("Email Attribute from HTTP{} ", httpServletRequest.getAttribute("mail"));


        // Do look up here:

        final String email = (String) httpServletRequest.getAttribute("mail");

        if (DatabaseInitializer.isDEBUG() == false) {
            final String[] allowedUsers = DatabaseInitializer.getUserList();

            final boolean foundUser = Arrays.stream(allowedUsers).anyMatch(email::equals);

            if (foundUser) {
                model.addAttribute(ACCESS, "yes");
                logger.debug("Access OK");
            } else {
                model.addAttribute(ACCESS, "no");
                logger.debug("Access failed");
            }

        } else {
            model.addAttribute(ACCESS, "yes");
            logger.debug("Access OK");
        }

        return model;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView greetingForm(final Item item) {
        final List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemService.save(itemList);

        final ModelAndView modelAndView = new ModelAndView("redirect:/results");
        return modelAndView;

    }

    /**
     * Returns information from ArchivesSpace for autopopulating component and resources boxes
     * @return ResponseBody
     * @throws URISyntaxException
     */
    // TODO clean up
    @RequestMapping("/api/**")
    @ResponseBody
    public ResponseEntity proxyASpace(@RequestBody(required = false) String body,
                                      HttpMethod method, HttpServletRequest request, HttpServletResponse servletResponse)
            throws URISyntaxException {
        // This ensures that the app path is not passed to ASpace
        final String requestUrl = request.getRequestURI().replace("/adml", "");

        logger.debug("Set connection:{}", request.getHeader("Connection"));

        URI uri = new URI("https", server, null, null);

        uri = UriComponentsBuilder.fromUri(uri)
                .path(requestUrl)
                .query(request.getQueryString())
                .build(false).toUri();


        final HttpHeaders headers = new HttpHeaders();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();

            // Note: Setting the headers to avoid gzip, keep-alive, chunked response!

            if (headerName.equalsIgnoreCase("Connection")) {
                logger.debug("Setting connection header to close to avoid chunked response");
                headers.set(headerName, "close");
            }

            else if (headerName.equalsIgnoreCase("Accept-Encoding")) {
                logger.debug("Setting accept encoding to identity to avoid chunked response");
                headers.set(headerName, "identity");
            }

            else {
                headers.set(headerName, request.getHeader(headerName));
            }
        }

        // Send the request to ASpace and send the response back to the client

        final HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        final RestTemplate restTemplate = new RestTemplate();

        try {
            final ResponseEntity rb = restTemplate.exchange(uri, method, httpEntity, String.class);
            logger.debug("Raw response from the server:" + rb.getBody().toString());

            // For "resources", just return the title (otherwise it was causing a truncated output error on the jquery side)
            try {
                if (requestUrl.contains("resources")) {
                    final JSONParser parser = new JSONParser();
                    logger.debug("Parsing just the resource title");
                    final JSONObject jsonObject = (JSONObject) parser.parse(rb.getBody().toString());
                    logger.debug("Parsed the response as JSON");
                    logger.debug("Title from the parsed JSON:" + jsonObject.get("title"));
                    final JSONObject response = new JSONObject();
                    response.put("title", jsonObject.get("title")); // this is the components field in the webapp
                    response.put("type", "adml_resource"); // to indicate what we are returning

                    final JSONArray instances = (JSONArray) jsonObject.get("instances");
                    final JSONObject inst = (JSONObject) instances.get(0);
                    final JSONObject subContainer = (JSONObject) inst.get("sub_container");
                    final JSONObject topContainer = (JSONObject) subContainer.get("top_container");

                    logger.debug("Top container:{}", topContainer.get("ref").toString());
                    response.put("top_container", topContainer.get("ref").toString());
                    response.put("cont", "abc");

                    return new ResponseEntity<>(response.toJSONString(), HttpStatus.OK);
                }
            } catch (ParseException e) {
                logger.error("Error reading JSON for resource:{} {}", requestUrl, e);
            }


            return rb;
        } catch (HttpStatusCodeException e) {
            logger.debug("Error" + e);
            return ResponseEntity.status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }

}