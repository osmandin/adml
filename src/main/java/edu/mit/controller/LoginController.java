package edu.mit.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class LoginController {

    private final Logger logger = getLogger(this.getClass());


    @RequestMapping(value = "/login1", method = RequestMethod.GET)
    public String greetingForm(final Model model) {
        //model.addAttribute("item", new Item());
        //model.addAttribute("username", username);
        return "login";
    }

}

