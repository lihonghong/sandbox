package com.hong.search.evaluate.cms.service;


import org.junit.Test;

import java.io.IOException;

/**
 * Created by hong on 11/26/15.
 */
public class SearchTest {
    @Test
    public void testSearchApi() throws IOException {
        SearchServiceImpl searchService = new SearchServiceImpl();
        System.out.println(searchService.assertSearchResult("小米", null));
    }
}
