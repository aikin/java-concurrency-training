package com.concurrency.wordcount;

import java.util.concurrent.BlockingQueue;

public class Parser implements Runnable {

    private final BlockingQueue<Page> pageBlockingQueue;

    Parser(BlockingQueue<Page> pageBlockingQueue) {
        this.pageBlockingQueue = pageBlockingQueue;
    }

    @Override
    public void run() {
        Pages pages = new Pages(100000, "enwiki.xml");
        for (Page page : pages) {
            try {
                pageBlockingQueue.put(page);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
