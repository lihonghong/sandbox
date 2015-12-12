package com.hong.pressure.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liyang
 * Date: 11/20/13
 * Time: 11:55 AM
 */
abstract public class SearchHandler extends Thread {
    private MessageQueue queue = null;
    private int successCount = 0;
    private List<String> failureQuery = new ArrayList<String>();
    private List<String> exceptionQuery = new ArrayList<String>();

    private int fetchNumber = 10;

    abstract protected void initInternal();

    abstract protected boolean search(String keywords) throws Exception;

    public void init(MessageQueue queue) {
        this.queue = queue;
        initInternal();
    }

    public void run() {
        while (!queue.isEmpty()) {
            List<String> queryList = queue.get(fetchNumber);
            System.out.println(queryList);
            for (String query : queryList) {
                if (query == null) {
                    continue;
                }
                try {
                    if (search(query)) {
                        ++successCount;
                    } else {
                        failureQuery.add(query);
                    }
                } catch (Exception e) {
                    exceptionQuery.add(query);
                    System.out.printf("Query: %s throw exception:%s\n", query, e.toString());
                }
            }
        }
    }

    public int getSuccessCount() {
        return successCount;
    }

    public List<String> getFailureQuery() {
        return failureQuery;
    }

    public List<String> getExceptionQuery() {
        return exceptionQuery;
    }
}
