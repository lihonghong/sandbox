package com.hong.search.evaluate.cms.filler;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import javax.management.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 11/26/15.
 */
public class HotQueryFiller implements Query {
    private static final String URL = "http://o.go2yd.com/oapi/xiaomi/hot_words";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static class HotWord {
        @Key("status")
        private String status;

        @Key("code")
        public int code;

        @Key("data")
        public List<String> data;
    }

    public List<String> fillImpl() {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });

        List<String> result = new ArrayList<>();
        HttpRequest request;
        try {
            request = requestFactory.buildGetRequest(new GenericUrl(URL));
            HttpResponse response = request.execute();
            HotWord hotWord = response.parseAs(HotWord.class);
            for (String d : hotWord.data) {
                result.add(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
