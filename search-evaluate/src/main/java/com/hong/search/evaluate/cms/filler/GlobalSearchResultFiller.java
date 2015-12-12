package com.hong.search.evaluate.cms.filler;

import com.alibaba.fastjson.JSONArray;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hong on 11/26/15.
 */
public class GlobalSearchResultFiller {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalSearchResultFiller.class);
    private static final String URL = "http://global.search.xiaomi.net/global/v3/sug";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static class SearchResult {
        @Key("status")
        private int status;

        @Key("count")
        public int count;

        @Key("result")
        public JSONArray result;
    }

    public JSONArray fillImpl(String query) {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });

        try {
            Map<String, String> data = Maps.newHashMap();
            data.put("q", query);
            data.put("s", "0");
            data.put("n", "20");
            HttpContent params = new UrlEncodedContent(data);
            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(URL), params);
            HttpResponse response = request.execute();
            SearchResult result = response.parseAs(SearchResult.class);
            return result.result;
        } catch (IOException e) {
            LOGGER.error("check empty result error, {}", e);
            return null;
        }
    }

}
