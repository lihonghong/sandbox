package com.hong.search.evaluate.cms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface SearchService {
    public void reset();

    public JSONObject getQuery();

    public JSONArray getNewsQuery();

    public JSONArray next(String query);

    public JSONObject getScore();

    public void submitChoice(String choice);

}

