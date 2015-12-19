package com.hong.search.evaluate.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.hong.search.evaluate.cms.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/evaluate")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/score", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getSearchScore() {
        return searchService.getScore();
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getSearchQuery() {
        return searchService.getQuery();
    }

    @RequestMapping(value = "/next/{query}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getNext(@PathVariable String query) {
        return (searchService.next(query));
    }

    @RequestMapping(value = "/submit/{choice}", method = RequestMethod.POST)
    @ResponseBody
    public void submitChoice(@PathVariable String choice) {
        searchService.submitChoice(choice);
    }

}
