package com.hong.search.evaluate.cms.service;


import com.hong.search.evaluate.cms.filler.GlobalSearchQueryFiller;
import com.hong.search.evaluate.cms.filler.LocalQueryFiller;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by hong on 11/26/15.
 */
public class LocalQueryFillerTest {
    @Test
    public void testLocalQueryApi() throws IOException {
        LocalQueryFiller globalSearchResultFiller = new LocalQueryFiller();
        List<String> list =globalSearchResultFiller.fillImpl();
        for (String query : list) {
            System.out.println(query);
        }
    }
}
