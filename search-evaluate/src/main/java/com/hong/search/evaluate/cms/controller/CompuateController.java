package com.hong.search.evaluate.cms.controller;

import com.hong.search.evaluate.cms.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/compute")
public class CompuateController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/score", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getSearchScore() {
        return searchService.getScore();
    }


    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public void reset() {
        searchService.reset();
    }

//    @RequestMapping("/layout")
//    public String getSearchPartialPage() {
//        return "compute/layout";
//    }
}
