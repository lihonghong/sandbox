package com.hong.search.evaluate.cms.filler;

import com.alibaba.fastjson.JSONArray;
import com.hong.search.evaluate.cms.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hong on 15-12-23.
 */
public class CheckSearchFiller {
    @Autowired
    private SearchService searchService;

    public void fill(){
        JSONArray jsonArray = searchService.getNewsQuery();
        System.out.println("task:"+jsonArray);
    }
}
