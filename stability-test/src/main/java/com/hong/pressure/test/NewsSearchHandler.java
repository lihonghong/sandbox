package com.hong.pressure.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaomi.miliao.thrift.ClientFactory;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
import com.xiaomi.search.global.thrift.CrawlerSearchRequest;
import com.xiaomi.search.global.thrift.GlobalCrawlerService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hong on 11/9/15.
 */
public class NewsSearchHandler extends SearchHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(NewsSearchHandler.class);
  private GlobalCrawlerService.Iface crawlerService = null;

  @Override
  protected void initInternal() {
    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.LUGU);
    crawlerService = ClientFactory.getClient(GlobalCrawlerService.Iface.class, 1000);
  }
  @Override
  protected boolean search(String keywords) throws TException {
    String news = crawlerService.searchNews(new CrawlerSearchRequest().setQuery(keywords)).getResult();
    if (valid(news)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean valid(String data) {
    try {
      JSONObject jsonData = JSONObject.parseObject(data);
      JSONArray result = jsonData.getJSONArray("result");
      if (result.size() == 0) {
        return false;
      }
    } catch (Exception e) {
      System.out.println("data: " + data + "valid fail");
      return false;
    }
    return true;
  }
}
