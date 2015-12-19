package com.hong.search.evaluate.cms.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hong on 15-12-19.
 */
public class NdcgTest {
    private static final String searchResult = "[\n" +
            "    [\n" +
            "        {\n" +
            "            \"group_name\": \"新闻\",\n" +
            "            \"items\": [\n" +
            "                {\n" +
            "                    \"data\": [\n" +
            "                        {\n" +
            "                            \"detail_url\": {\n" +
            "                                \"min_version\": 69,\n" +
            "                                \"package\": \"com.yidian.xiaomi\",\n" +
            "                                \"style\": \"text\",\n" +
            "                                \"text\": \"详情\",\n" +
            "                                \"url\": \"yidian://open_news?docid=0BiXEfAg&s=misearch\"\n" +
            "                            },\n" +
            "                            \"id\": \"0BiXEfAg\",\n" +
            "                            \"package\": \"com.yidian.xiaomi\",\n" +
            "                            \"subtitle\": \"7小时前 | 手机人民网\",\n" +
            "                            \"title\": \"北京军区组织实兵对抗演习坚持从难从严设置环境由“贴近实战”向“突破极限”转变\",\n" +
            "                            \"url\": {\n" +
            "                                \"min_version\": 0,\n" +
            "                                \"package\": \"com.android.browser\",\n" +
            "                                \"text\": \"浏览\",\n" +
            "                                \"url\": \"http://www.yidianzixun.com/n/0BiXEfAg?s=misearch\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"style\": \"title_2line\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"data\": [\n" +
            "                        {\n" +
            "                            \"detail_url\": {\n" +
            "                                \"min_version\": 69,\n" +
            "                                \"package\": \"com.yidian.xiaomi\",\n" +
            "                                \"style\": \"text\",\n" +
            "                                \"text\": \"详情\",\n" +
            "                                \"url\": \"yidian://open_news?docid=0BiSbqWp&s=misearch\"\n" +
            "                            },\n" +
            "                            \"id\": \"0BiSbqWp\",\n" +
            "                            \"image\": \"http://i3.go2yd.com/image.php?url=0BiSbq00&type=_242x175\",\n" +
            "                            \"image_fit\": \"center_crop\",\n" +
            "                            \"package\": \"com.yidian.xiaomi\",\n" +
            "                            \"subtitle\": \"15小时前 | 搞机者\",\n" +
            "                            \"title\": \"iOS9六个隐藏秘技及设置： 教你如何关闭最那些烦人的应用！\",\n" +
            "                            \"u                               rl\": {\n" +
            "                                \"min_version\": 0,\n" +
            "                                \"package\": \"com.android.browser\",\n" +
            "                                \"text\": \"浏览\",\n" +
            "                                \"url\": \"http://www.yidianzixun.com/n/0BiSbqWp?s=misearch\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"style\": \"cover_2line\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"data\": [\n" +
            "                        {\n" +
            "                            \"detail_url\": {\n" +
            "                                \"min_version\": 69,\n" +
            "                                \"package\": \"com.yidian.xiaomi\",\n" +
            "                                \"style\": \"text\",\n" +
            "                                \"text\": \"详情\",\n" +
            "                                \"url\": \"yidian://open_news?docid=0BiRNklW&s=misearch\"\n" +
            "                            },\n" +
            "                            \"id\": \"0BiRNklW\",\n" +
            "                            \"image\": \"http://i3.go2yd.com/image.php?url=0BiRNk00&type=_242x175\",\n" +
            "                            \"image_fit\": \"center_crop\",\n" +
            "                            \"package\": \"com.yidian.xiaomi\",\n" +
            "                            \"subtitle\": \"17小时前 | 兔玩网\",\n" +
            "                            \"title\": \"LOL怎么设置智能施法 LOL智能施法怎么看技能范围\",\n" +
            "                            \"url\": {\n" +
            "                                \"min_version\": 0,\n" +
            "                                \"package\": \"com.android.browser\",\n" +
            "                                \"text\": \"浏览\",\n" +
            "                                \"url\": \"http://www.yidianzixun.com/n/0BiRNklW?s=misearch\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"style\": \"cover_2line\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"package\": \"com.yidian.xiaomi\",\n" +
            "            \"type\": \"news\"\n" +
            "        }\n" +
            "    ]\n" +
            "]";

    @Test
    public void scoreTest() {
        Ndcg ndcg = new Ndcg();
        Map<String, JSONObject> choice = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("0BiXEfAg", 1);
        jsonObject.put("0BiSbqWp", 2);
        jsonObject.put("0BiRNklW", 0);
        choice.put("test", jsonObject);
        Map<String, JSONArray> data = new HashMap<>();
        data.put("test", new JSONArray(JSONArray.parseArray(searchResult)));
        System.out.println(ndcg.score(choice, data));
    }

    @Test
    public void ndcgTest() {
        List<Integer> rank = Arrays.asList(3, 2, 3, 0, 1, 2);
        Ndcg ndcg = new Ndcg();
        System.out.println(ndcg.computedcg(rank)/ndcg.computeIDCG(rank));
    }

}
