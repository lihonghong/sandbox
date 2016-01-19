package com.hong.search.evaluate.cms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hong.search.evaluate.cms.filler.GlobalSearchResultFiller;
import com.hong.search.evaluate.cms.filler.NewsHotQueryFiller;
import com.hong.search.evaluate.cms.filler.LocalQueryFiller;
import com.hong.search.evaluate.cms.utils.Ndcg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SearchService")
public class SearchServiceImpl implements SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
    private static int offset = 0;
    @Autowired
    private LocalQueryFiller localQueryFiller;
    @Autowired
    private GlobalSearchResultFiller globalSearchResultFiller;
    @Autowired
    private NewsHotQueryFiller newsHotQueryFiller;

    private List<String> list = null;
    private Map<String, JSONArray> dataMap = new HashMap<>();
    private Map<String, JSONObject> choiceMap = new HashMap<>();

    @Override
    public void reset() {
        offset = 0;
        choiceMap.clear();
        dataMap.clear();
    }

    @Override
    public JSONObject getQuery() {
        JSONObject jsonObject = new JSONObject();
        if (list == null) {
            list = localQueryFiller.fillImpl();
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
        Ndcg ndcg = new Ndcg();
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

    @Override
    public JSONArray getNewsQuery() {
        JSONArray jsonArray = new JSONArray();
        List<String> list = newsHotQueryFiller.fillImpl();
        for (String query : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", query);
            boolean status = assertSearchResult(query, "news");
            jsonObject.put("status", status);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public JSONArray getHotQuery() {
        JSONArray jsonArray = new JSONArray();
        List<String> list = newsHotQueryFiller.fillImpl();
        for (String query : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", query);
            boolean status = assertSearchResult(query, "news");
            jsonObject.put("status", status);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public boolean assertSearchResult(String query, String filterType) {
        JSONArray result = globalSearchResultFiller.fillImpl(query);
        try {
            for (int i = 0; i < result.size(); i++) {
                JSONObject group = result.getJSONObject(i);
                JSONArray items = group.getJSONArray("items");
                String type = group.getString("type");
                if (type != null && !type.equals(filterType)) {
                    continue;
                }
                for (int k = 0; k < items.size(); k++) {
                    JSONObject item = items.getJSONObject(k);
                    JSONArray dataArray = item.getJSONArray("data");
                    for (int z = 0; z < dataArray.size(); z++) {
                        JSONObject detail = dataArray.getJSONObject(z);
                        String title = detail.getString("title");
                        LOGGER.debug("assert query {} success! one is {}", query, title);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("query search fail", e);
        }
        LOGGER.debug("assert query {} fail!", query);
        return false;
    }
}
