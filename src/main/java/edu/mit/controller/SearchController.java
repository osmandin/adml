package edu.mit.controller;

import edu.mit.domain.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SearchController {

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String greetingForm(final Model model) {
        //model.addAttribute("item", new Item());
        return "results";
    }
}

