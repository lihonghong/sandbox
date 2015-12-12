package com.hong.cache;

import com.xiaomi.miliao.xcache.XCache;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hong on 10/9/15.
 */
public class NewsCache {
  protected static final Logger LOGGER = LoggerFactory.getLogger(NewsCache.class);
  private static final String newsCacheName = "global-search.news";
  protected static XCache<String> httpCache = null;

  static {
    ZKFacade.getZKSettings().setEnviromentType(EnvironmentType.LUGU);
    httpCache = XCache.getCache(newsCacheName, String.class, false);
  }

  public static void setCache(String key, String value) {
    if (key != null && value != null) {
      try {
        boolean success = httpCache.set(key, value, XCache.EXPIRE_MINUTE * 30);
        System.out.println("cache key" + key + ",success:" + success + ",zk env:" + ZKFacade.getZKSettings().getEnvironmentType());
      } catch (Exception e) {
        LOGGER.error("cache set fail in http ", e);
        System.out.println(e);
      }
    }
  }
}
