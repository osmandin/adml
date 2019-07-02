package edu.mit.adml.controller;

import edu.mit.adml.DatabaseInitializer;
import edu.mit.adml.domain.Item;
import edu.mit.adml.repository.ItemRepository;
import edu.mit.adml.service.ItemService;
import edu.mit.adml.util.Util;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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

    @RequestMapping("/api/**")
    @ResponseBody
    public ResponseEntity mirrorRest(@RequestBody(required = false) String body,
                                     HttpMethod method, HttpServletRequest request, HttpServletResponse response)
            throws URISyntaxException {
        //String requestUrl = request.getRequestURI().replace("/adml/api", "");
        String requestUrl = request.getRequestURI().replace("/adml", "");

        logger.debug("Set connection:{}", request.getHeader("Connection"));


        //logger.info("Here for:{}", requestUrl);


        //URI uri = new URI("http", null, server, port, null, null, null);

        URI uri = new URI("https", server, null, null);


        uri = UriComponentsBuilder.fromUri(uri)
                .path(requestUrl)
                .query(request.getQueryString())
                .build(false).toUri();


        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.debug("Considering header:" + headerName);

            headers.set(headerName, request.getHeader(headerName));

            if (headerName.equalsIgnoreCase("Connection")) {
                logger.debug("Setting connection header to close:");
                headers.set(headerName, "close");
            }

            if (headerName.equalsIgnoreCase("Accept-Encoding")) {
                logger.debug("Setting accept encoding to close:");
                headers.set(headerName, "identity");
            }
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());

        // https://stackoverflow.com/questions/34415144/how-to-parse-gzip-encoded-response-with-resttemplate-from-spring-web
        RestTemplate restTemplate = new RestTemplate();

        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        JSONParser parser = new JSONParser();


        try {
            final ResponseEntity rb = restTemplate.exchange(uri, method, httpEntity, String.class);
            logger.debug("Response from the server:" + rb.toString());

            // Parses and reads JSON:
            try {
                if (requestUrl.contains("resources")) {
                    logger.debug("Just will returning resource title");
                    /*JSONObject jsonObject = (JSONObject) parser.parse(rb.getBody().toString().
                            replace("<200,", "").replace("]}", "")
                    .replaceAll("http://", "http"));*/

                    final JSONObject jsonObject = (JSONObject) parser.parse(rb.getBody().toString());

                    logger.debug("Parsed json object");

                    logger.debug("Extracted title" + jsonObject.get("title"));

                    JSONObject newJsonObject = new JSONObject();
                    newJsonObject.put("title", jsonObject.get("title"));

                    return new ResponseEntity<String>(newJsonObject.toJSONString(), HttpStatus.OK);
                }
            } catch (Exception e) {
                logger.error("Error reading or returning json:" + e);
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