package edu.mit.controller;

import edu.mit.domain.Item;
import edu.mit.domain.Pager;
import edu.mit.repository.ItemRepository;
import edu.mit.service.ItemService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


@Controller
public class QuickSearchResultsController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 15};

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
    @RequestMapping(value={"/", "results"}, method = RequestMethod.GET)
    public ModelAndView showItemsPage(@ModelAttribute Search search,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                      @RequestParam(value = "page", required = false) Integer page) {

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
        return modelAndView;
    }


}
