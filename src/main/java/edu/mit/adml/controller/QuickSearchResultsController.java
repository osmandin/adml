package edu.mit.adml.controller;

import edu.mit.adml.DatabaseInitializer;
import edu.mit.adml.domain.Pager;
import edu.mit.adml.domain.Item;
import edu.mit.adml.repository.ItemRepository;
import edu.mit.adml.service.ItemService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class QuickSearchResultsController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 25;
    private static final int[] PAGE_SIZES = {25, 50, 100};
    public static final String ACCESS = "access";

    private ItemService itemService;

    private final Logger logger = getLogger(this.getClass());


    @Autowired
    public QuickSearchResultsController(ItemService studentService) {
        this.itemService = studentService;
    }

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Handles all requests
     *
     * @param pageSize
     * @param page
     * @return model and view
     */
    @RequestMapping(value = {"/", "results"}, method = RequestMethod.GET)
    public ModelAndView showItemsPage(@ModelAttribute Search search,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                      @RequestParam(value = "page", required = false) Integer page, HttpServletRequest httpServletRequest) {

        final ModelAndView modelAndView = new ModelAndView("results");
        final int evalPageSize = pageSize == null ? INITIAL_PAGE_SIZE : pageSize;
        final int evalPage = (page == null || page < 1) ? INITIAL_PAGE : page - 1;
        final PageRequest p = new PageRequest(evalPage, evalPageSize,
                Sort.Direction.ASC, "itemId", "format");
        final Page<Item> results = itemRepository.findAll(p);

        if (results == null) {
            logger.info("No results found.");
        }

        final Pager pager = new Pager(results.getTotalPages(), results.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("items", results);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("numberResults", results.getTotalElements());

        // Do look up here:

        String email = (String) httpServletRequest.getAttribute("mail");

        if (email == null || email.isEmpty()) {
            email = httpServletRequest.getHeader("mail");
        }

        if (email == null || email.isEmpty()) {
            //modelAndView.addObject(ACCESS, "no");
            logger.debug("Access failed for Touchstone");
            return modelAndView;
        }


        final String[] allowedUsers = DatabaseInitializer.getUserList();

        //TODO extract this logic:

        boolean userFound = false;

        for (final String s : allowedUsers) {
            // logger.debug("Matching against authorized user:{}", s);
            if (email.equalsIgnoreCase(s)) {
                userFound = true;
            }
        }


        if (userFound) {
            modelAndView.addObject(ACCESS, "yes");
            logger.debug("Access OK");
        } else {
            //modelAndView.addObject(ACCESS, "no");
            logger.debug("Access failed");
        }


        return modelAndView;
    }


}
