package edu.mit.aspace;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/token")
public class BasePoint {

    private static String s = Aspace.authenticate(); //TODO change

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String token()  {
        return s;
    }

}