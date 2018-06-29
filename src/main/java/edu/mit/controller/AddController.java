package edu.mit.controller;

import edu.mit.DatabaseInitializer;
import edu.mit.domain.Item;
import edu.mit.domain.Pager;
import edu.mit.repository.ItemRepository;
import edu.mit.service.ItemService;
import edu.mit.util.Util;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class AddController {

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

        ModelAndView modelAndView = new ModelAndView("add");

        // Page<Item> items = itemService.findByItemId(id, new PageRequest(0, 1));

        // TODO: decide where things like go:

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

        if (email.equals("osmandin@mit.edu") || email.equals("smithkr@mit.edu") || email.equals("carrano@mit.edu")) {
            model.addAttribute("access", "yes");
            logger.info("Access OK");
        } else {
            model.addAttribute("access", "no");
            logger.info("Access failed");
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