package com.hong.search.evaluate.cms.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by hong on 15-12-19.
 */
public class Ndcg {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ndcg.class);
    private double score = 0;
    private int defaultLevel = 4;

    public double score(Map<String, JSONObject> choice, Map<String, JSONArray> data) {
        for (Map.Entry entry : data.entrySet()) {
            String query = (String) entry.getKey();
            JSONArray searchResult = (JSONArray) entry.getValue();
            JSONObject choiceResult = choice.get(query);
            try {
                double ndcgScore = compute(searchResult, choiceResult);
                score += ndcgScore;
            } catch (Exception e) {
                LOGGER.error("maybe search fail", e);
            }
        }

        if (data.size() > 0) {
            score /= data.size();
        }
        return score;
    }

    private double compute(JSONArray searchResult, JSONObject choiceResult) {
        List<String> docIdList = parseSearchData(searchResult);
        if (choiceResult == null) {
            choiceResult = new JSONObject();
        }
        Map<String, Integer> choiceMap = parseChoiceData(choiceResult);

        List<Integer> rank = new ArrayList<>();
        for (String id : docIdList) {
            if (choiceMap.containsKey(id)) {
                rank.add(choiceMap.get(id));
            } else {
                rank.add(defaultLevel);
            }
        }

        double dcg = computedcg(rank);
        double idcg = computeIDCG(rank);
        double ndcg = dcg / idcg;
        System.out.println(dcg + "," + idcg + "," + ndcg);
        return ndcg;
    }

    private List<String> parseSearchData(JSONArray searchResult) {
        List<String> docIdList = new ArrayList<>();
        for (int i = 0; i < searchResult.size(); i++) {
            JSONObject group = searchResult.getJSONObject(i);
            JSONArray items = group.getJSONArray("items");
            for (int k = 0; k < items.size(); k++) {
                JSONObject item = items.getJSONObject(k);
                JSONArray dataArray = item.getJSONArray("data");
                for (int z = 0; z < dataArray.size(); z++) {
                    JSONObject detail = dataArray.getJSONObject(z);
                    String id = detail.getString("id");
                    docIdList.add(id);
                }

            }
        }

        return docIdList;
    }

    private Map<String, Integer> parseChoiceData(JSONObject choiceResult) {
        Map<String, Integer> map = new HashMap<>();
        Set<String> set = choiceResult.keySet();
        for (String key : set) {
            map.put(key, choiceResult.getInteger(key));
        }
        return map;
    }

    public double computedcg(List<Integer> list) {
        double idcg = 0;
        for (int i = 0; i < list.size(); i++) {
            int rel = list.get(i);
            idcg += rel / logBase2(i + 1);
        }
        return idcg;

    }

    public double computeIDCG(List<Integer> list) {
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer t1, Integer t2) {
                return t2.compareTo(t1);
            }
        });
        return computedcg(list);
    }

    private double logBase2(double value) {
        if (value == 1) {
            return 1;
        }
        return Math.log(value) / Math.log(2);
    }

    public void reset() {
        score = 0;
    }
}
