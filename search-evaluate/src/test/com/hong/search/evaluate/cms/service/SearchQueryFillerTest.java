package com.hong.search.evaluate.cms.service;


import com.hong.search.evaluate.cms.filler.GlobalSearchQueryFiller;
import com.hong.search.evaluate.cms.filler.GlobalSearchResultFiller;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hong on 11/26/15.
 */
public class SearchQueryFillerTest {
    @Test
    public void testSearchApi() throws IOException {
        GlobalSearchQueryFiller globalSearchResultFiller = new GlobalSearchQueryFiller();
        System.out.println(globalSearchResultFiller.fillImpl());
    }
}
