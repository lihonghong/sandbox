package com.hong.pressure;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hong.utils.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpHandler extends Handler {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpHandler.class);
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private String url = "https://m.baidu.com/s?word=";

  @Override
  protected void initInternal() {
  }

  @Override
  protected boolean search(String keywords) throws Exception {
    if (request(keywords) != null) {
      return true;
    } else {
      return false;
    }
  }

  public String request(String query) {
    HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setParser(new JsonObjectParser(JSON_FACTORY));
      }
    });

    try {
      HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(url + query));
      HttpResponse response = request.execute();
      return response.parseAsString();
    } catch (Exception e) {
      System.out.println(e);
      LOGGER.error("check empty result error, {}", e);
      return null;
    }
  }

}
