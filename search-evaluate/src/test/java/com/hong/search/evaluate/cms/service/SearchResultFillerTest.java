package com.hong.search.evaluate.cms.service;


import com.hong.search.evaluate.cms.filler.GlobalSearchResultFiller;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hong on 11/26/15.
 */
public class SearchResultFillerTest {
    @Test
    public void testSearchApi() throws IOException {
        GlobalSearchResultFiller globalSearchResultFiller = new GlobalSearchResultFiller();
        System.out.println(globalSearchResultFiller.fillImpl("美国枪击案"));
    }
}
