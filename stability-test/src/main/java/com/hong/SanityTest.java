package com.hong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 11/9/15.
 */
public class SanityTest {
    private List<SearchHandler> handlers = new ArrayList<SearchHandler>();
    private MessageQueue queue = null;
    private static int threadNumber = 10;

    public <T> void init(List<SearchHandler> handlers) {
        this.handlers = handlers;
        queue = new MessageQueue<String>();
        for (SearchHandler handler : handlers) {
            handler.init(queue);
        }
    }

    public void process() {
        for (int i = 0; i < handlers.size(); ++i) {
            System.out.printf("Start thread %d\n", i);
            handlers.get(i).start();
        }
    }

    public static void main(String[] args) {
        SanityTest sanityTest = new SanityTest();
        List<SearchHandler> handlers = new ArrayList<SearchHandler>();
        for (int i = 0; i < threadNumber; ++i) {
            NewsSearchHandler handler = new NewsSearchHandler();
            handlers.add(handler);
        }
        sanityTest.init(handlers);
        sanityTest.process();
    }
}
