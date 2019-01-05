package edu.mit.adml.aspace;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequestMapping("/token")
public class BasePoint {

    private static String s = Aspace.authenticate();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String token()  {
        return s;
    }

}