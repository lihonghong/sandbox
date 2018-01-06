package com.hong;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by hong on 10/9/15.
 */
public class LogTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

  @Test
  public void test() {
    LOGGER.debug("hello world");
  }
}
