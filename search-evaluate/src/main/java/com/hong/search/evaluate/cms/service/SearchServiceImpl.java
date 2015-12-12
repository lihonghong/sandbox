package com.hong.search.evaluate.cms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hong.search.evaluate.cms.filler.GlobalSearchResultFiller;
import com.hong.search.evaluate.cms.filler.HotQueryFiller;
import com.hong.search.evaluate.cms.filler.LocalQueryFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SearchService")
public class SearchServiceImpl implements SearchService {
    @Autowired
    private LocalQueryFiller hotQueryFiller;

    @Autowired
    private GlobalSearchResultFiller globalSearchResultFiller;

    private static int offset = 0;
    private List<String> list = null;

    @Override
    public void reset() {
        offset = 0;
    }

    @Override
    public JSONObject getQuery() {
        JSONObject jsonObject = new JSONObject();
        if (list == null) {
            list = hotQueryFiller.fillImpl();
        }

        if (offset < list.size()) {
            jsonObject.put("query", list.get(offset));
            offset++;
        }
        return jsonObject;
    }

    @Override
    public JSONArray next(String query) {
        return (globalSearchResultFiller.fillImpl(query));
    }

    @Override
    public JSONObject getScore() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", 0.1);
        return jsonObject;
    }

    @Override
    public void submitChoice(String choice) {
        System.out.println(choice + "-json");
    }
}
