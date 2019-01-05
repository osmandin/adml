package edu.mit.adml.controller;

import edu.mit.adml.util.Util;
import edu.mit.adml.domain.Item;
import edu.mit.adml.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
public class SingleItemController {

    private ItemService itemService;

    @Autowired
    public SingleItemController(ItemService studentService) {
        this.itemService = studentService;
    }

    @RequestMapping(value = "/singleitem", method = RequestMethod.GET)
    public String greetingForm(final Model model,
                               @RequestParam(value = "id", required = false) Long id) {

        Item item;

        ModelAndView modelAndView = new ModelAndView("singleitem");


        if (id != null) {
            Page<Item> items = itemService.findByItemId(id, new PageRequest(0, 1));

            final Map<Integer, String> formats= Util.getFormats();

            model.addAttribute("format", formats);

            final Map<Integer, String> transferStatus= Util.getTransferStatus();
            model.addAttribute("transferStatus", transferStatus);

            final Map<Integer, String> transferMethods = Util.getTransferMethods();
            model.addAttribute("transferMethod", transferMethods);

            final Map<Integer, String> itemDisposition = Util.getDispositions();
            model.addAttribute("disposition", itemDisposition);


            if (items.getTotalElements() == 0) {
                item = new Item();
            } else {
                item = items.iterator().next();
            }

            model.addAttribute("item", item);
            return "singleitem";
        }
        return "error";
    }


    @RequestMapping(value = "/singleitem", method = RequestMethod.POST)
    public ModelAndView greetingForm(final Item item) {
        item.setUpdated(new Date());

        List<Item> items = new ArrayList<>();
        items.add(item);
        itemService.save(items);
        System.out.println("items updated");

        ModelAndView modelAndView = new ModelAndView("redirect:/results");
        modelAndView.addObject("id", item.getItemId());
        return modelAndView;

    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(       Date.class,
                new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));
    }

}