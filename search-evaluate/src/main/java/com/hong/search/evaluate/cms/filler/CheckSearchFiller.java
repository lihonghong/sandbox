package com.hong.search.evaluate.cms.filler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hong.search.evaluate.cms.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hong on 15-12-23.
 */
public class CheckSearchFiller {
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckSearchFiller.class);
    private static final String URL = "http://support.d.xiaomi.net/mail/send";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    @Autowired
    private SearchService searchService;

    public static void main(String[] args) {
        CheckSearchFiller checkSearchFiller = new CheckSearchFiller();
        checkSearchFiller.sendEmail("test2", "test2", "lihonghong@xiaomi.com");
    }

    public void fill() {
        JSONArray jsonArray = searchService.getNewsQuery();
        String result = generateContent(jsonArray);
        if (result != null) {
            sendEmail("全局搜索热词golden set", result, "lihonghong@xiaomi.com");
            LOGGER.debug("send email");
        }
        LOGGER.debug("task: {}", jsonArray);
    }

    public void sendEmail(String title, String body, String address) {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                httpRequest.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });

        try {
            Map<String, String> postData = new HashMap<>();
            postData.put("title", title);
            postData.put("body", body);
            postData.put("address", address);
            postData.put("locale", "");
            postData.put("mailFrom", "noreply@app.xiaomi.com");
            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(URL), new UrlEncodedContent(postData));
            HttpResponse response = request.execute();
            LOGGER.debug("email response: {}", response.parseAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateContent(JSONArray jsonArray) {
        int failCount = 0;
        List<String> failQuery = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String query = jsonObject.getString("query");
            boolean success = jsonObject.getBoolean("status");
            if (!success) {
                failCount++;
                failQuery.add(query);
            }
        }

        if (jsonArray.size() == 0) {
            return null;
        }
        double failRatio = failCount / (double) jsonArray.size();
        if (failRatio > 0) {
            return generateHtml(failQuery, failRatio);
        }
        return null;
    }

    private String generateHtml(List<String> failQuery, double failRatio) {
        String head = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body>";
        String foot = "</body>\n" +
                "</html>";
        String content = "测试失败比例：" + String.format("%.2f", failRatio);
        for (String query : failQuery) {
            content += "<p>" + query + "</p>";
        }
        return head + content + foot;
    }

}
