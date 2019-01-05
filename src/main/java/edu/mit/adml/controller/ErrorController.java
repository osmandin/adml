package edu.mit.adml.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class ErrorController {

    private final Logger logger = getLogger(this.getClass());

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String greetingForm(Model model) {
        logger.info("Returning error page");
        return "error";
    }
}

