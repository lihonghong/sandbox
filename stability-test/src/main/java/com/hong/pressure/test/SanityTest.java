package com.hong.pressure.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 11/9/15.
 */
public class SanityTest {
    private List<SearchHandler> handlers = new ArrayList<SearchHandler>();
    private MessageQueue queue = null;
    private static int threadNumber = 10;
    private int totalNumber = 0;
    private int successCount = 0;
    private List<String> failureQuery = new ArrayList<String>();
    private List<String> exceptionQuery = new ArrayList<String>();

    private String failureFileName = "failure_query.txt";
    private String exceptionFileName = "exception_query.txt";

    public <T> void init(List<SearchHandler> handlers) {
        this.handlers = handlers;
        queue = new MessageQueue<String>();
        for (SearchHandler handler : handlers) {
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
        List<String> queryList = FileUtils.readLines(new File(fileName));
        totalNumber = queryList.size();
        for (int i = 0; i < 1; i++) {
            for (String query : queryList) {
                queue.put(query);
            }
        }
    }

    public void generateReport() throws IOException {
        if (!failureQuery.isEmpty()) {
            FileUtils.writeLines(new File(failureFileName), failureQuery);
        }
        if (!exceptionQuery.isEmpty()) {
            FileUtils.writeLines(new File(exceptionFileName), exceptionQuery);
        }

        System.out.printf("total:%d\n", totalNumber);
        System.out.printf("Success count:%d\n", successCount);
        System.out.printf("Failure count:%d\n", failureQuery.size());
        System.out.printf("Exception count:%d\n", exceptionQuery.size());
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        SanityTest sanityTest = new SanityTest();
        List<SearchHandler> handlers = new ArrayList<SearchHandler>();
        for (int i = 0; i < threadNumber; ++i) {
            NewsSearchHandler handler = new NewsSearchHandler();
            handlers.add(handler);
        }
        sanityTest.init(handlers);
        sanityTest.process("/tmp/query.txt");
        sanityTest.generateReport();
    }
}
