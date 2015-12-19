package com.hong.search.evaluate.cms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hong.search.evaluate.cms.filler.GlobalSearchResultFiller;
import com.hong.search.evaluate.cms.filler.LocalQueryFiller;
import com.hong.search.evaluate.cms.utils.Ndcg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SearchService")
public class SearchServiceImpl implements SearchService {
    private static int offset = 0;
    @Autowired
    private LocalQueryFiller hotQueryFiller;
    @Autowired
    private GlobalSearchResultFiller globalSearchResultFiller;
    private List<String> list = null;
    private Map<String, JSONArray> dataMap = new HashMap<>();
    private Map<String, JSONObject> choiceMap = new HashMap<>();
    private Ndcg ndcg = new Ndcg();

    @Override
    public void reset() {
        offset = 0;
        ndcg.reset();
        choiceMap.clear();
        dataMap.clear();
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
        JSONArray result = globalSearchResultFiller.fillImpl(query);
        dataMap.put(query, result);
        return result;
    }

    @Override
    public JSONObject getScore() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", ndcg.score(choiceMap, dataMap));
        return jsonObject;
    }

    @Override
    public void submitChoice(String choice) {
        JSONObject jsonObject = JSON.parseObject(choice);
        String query = jsonObject.getString("query");
        jsonObject.remove("query");
        choiceMap.put(query, jsonObject);
    }
}
