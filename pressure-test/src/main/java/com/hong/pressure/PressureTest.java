package com.hong.pressure;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharSink;
import com.google.common.io.LineProcessor;
import com.hong.utils.MessageQueue;
import com.hong.utils.Handler;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PressureTest {
  private List<Handler> handlers = new ArrayList<Handler>();
  private MessageQueue queue = null;
  private int totalNumber = 0;
  private int successCount = 0;
  private List<String> failureQuery = new ArrayList<>();
  private List<String> exceptionQuery = new ArrayList<>();

  private static int threadNumber = 1;
  private static String inputFilePath = "/tmp/query.txt";
  private static String failureFilePath = "/tmp/failure_query.txt";
  private static String exceptionFilePath = "/tmp/exception_query.txt";

  public <T> void init(List<Handler> handlers) {
    this.handlers = handlers;
    queue = new MessageQueue<String>();
    for (Handler handler : handlers) {
      handler.init(queue);
    }
  }

  public void process(String fileName) throws InterruptedException, IOException {
    readQuery(fileName);

    for (int i = 0; i < handlers.size(); ++i) {
      System.out.printf("Start thread %d\n", i);
      handlers.get(i).start();
    }

    // Thread join.
    for (int i = 0; i < handlers.size(); ++i) {
      handlers.get(i).join();
    }

    for (int i = 0; i < handlers.size(); ++i) {
      successCount += handlers.get(i).getSuccessCount();
      failureQuery.addAll(handlers.get(i).getFailureQuery());
      exceptionQuery.addAll(handlers.get(i).getExceptionQuery());
    }
  }

  private void readQuery(String fileName) throws IOException {
    List<String> queryList = readFile(fileName);

    totalNumber = queryList.size();
    for (int i = 0; i < 1; i++) {
      for (String query : queryList) {
        queue.put(query);
      }
    }
  }

  public void generateReport(String failureFilePath, String exceptionFilePath) throws IOException {
    if (!failureQuery.isEmpty()) {
      writeFile(failureFilePath, failureQuery);
    }
    if (!exceptionQuery.isEmpty()) {
      writeFile(exceptionFilePath, exceptionQuery);
    }

    System.out.printf("total:%d\n", totalNumber);
    System.out.printf("Success count:%d\n", successCount);
    System.out.printf("Failure count:%d\n", failureQuery.size());
    System.out.printf("Exception count:%d\n", exceptionQuery.size());
  }

  public static List<String> readFile(String fileName) throws IOException {
    return Files.readLines(new File(fileName), Charsets.UTF_8, new LineProcessor<List<String>>() {
      List<String> result = Lists.newArrayList();

      public boolean processLine(String line) throws IOException {
        result.add(line);
        return true;
      }

      public List<String> getResult() {
        return result;
      }
    });
  }

  public static void writeFile(String fileName, List<String> results) throws IOException {
    File file = new File(fileName);
    CharSink sink = Files.asCharSink(file, Charsets.UTF_8);
    sink.writeLines(results, "\n");
  }

  public static void main(String[] args) throws InterruptedException, IOException {
    PressureTest pressureTest = new PressureTest();
    List<Handler> handlers = new ArrayList<Handler>();
    for (int i = 0; i < threadNumber; ++i) {
      HttpHandler handler = new HttpHandler();
      handlers.add(handler);
    }

    System.out.printf("args:" + Arrays.toString(args));
    if (args.length >= 1) {
      inputFilePath = args[0];
    }
    if (args.length >= 2) {
      threadNumber = Integer.parseInt(args[1]);
    }

    pressureTest.init(handlers);
    pressureTest.process(inputFilePath);
    pressureTest.generateReport(failureFilePath, exceptionFilePath);
  }
}
