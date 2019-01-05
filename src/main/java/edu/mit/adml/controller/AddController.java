package edu.mit.adml.controller;

import edu.mit.adml.DatabaseInitializer;
import edu.mit.adml.util.Util;
import edu.mit.adml.domain.Item;
import edu.mit.adml.repository.ItemRepository;
import edu.mit.adml.service.ItemService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class AddController {

    public static final String ACCESS = "access";
    private final Logger logger = getLogger(this.getClass());

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

}