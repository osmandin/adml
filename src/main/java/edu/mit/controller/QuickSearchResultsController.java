package edu.mit.controller;

import edu.mit.domain.Item;
import edu.mit.domain.Pager;
import edu.mit.repository.ItemRepository;
import edu.mit.service.ItemService;
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


@Controller
public class QuickSearchResultsController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 25;
    private static final int[] PAGE_SIZES = {25, 50, 100};

    private ItemService itemService;

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
                                      @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "keywords", required = false) String keywords) {

        ModelAndView modelAndView = new ModelAndView("results");

        int evalPageSize = pageSize == null ? INITIAL_PAGE_SIZE : pageSize;

        int evalPage = (page == null || page < 1) ? INITIAL_PAGE : page - 1;

        String keywordOption = search.getKeywordOption();
        // System.out.println("Keywords option" + keywordOption);

        String keywordsStr = "";
        if (search != null && search.getContent() != null)
            keywordsStr = search.getContent().toLowerCase();

        // System.out.println("Keywords:" + keywordsStr);



        // Retrieval of actual content
        //final Page<Item> results = itemService.findAll(spec, new PageRequest(evalPage, evalPageSize,
        //        Sort.Direction.ASC, "fullName", "title"));

        PageRequest p = new PageRequest(evalPage, evalPageSize,
                Sort.Direction.ASC, "itemId", "format");

        final Page<Item> results = itemRepository.findAll(p);

        if (results == null) {
            System.out.println("Results null");
        }

        final Pager pager = new Pager(results.getTotalPages(), results.getNumber(), BUTTONS_TO_SHOW);


        modelAndView.addObject("items", results);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("keywords", keywords);
        modelAndView.addObject("keywordsOption", keywordOption);
        modelAndView.addObject("numberResults", results.getTotalElements());
        return modelAndView;
    }


}
