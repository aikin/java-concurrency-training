package com.concurrency.wordcount;

import java.util.concurrent.*;

public class WordCounter {

    private WordCounter() {
        throw new IllegalAccessError("Utility class");
    }

    private static final int NUM_COUNTERS = 4;

    public static void main(String[] args) throws InterruptedException {

        final BlockingQueue<Page> pageBlockingQueue = new ArrayBlockingQueue<>(100);
        final ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();

        Thread parserThread = new Thread(new Parser(pageBlockingQueue), "parser");

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < NUM_COUNTERS; i++) {
            executorService.execute(new Counter(pageBlockingQueue, counts));
        }

        long start = System.currentTimeMillis();

        parserThread.start();
        parserThread.join();

        for (int i = 0; i < NUM_COUNTERS; ++i) {
            pageBlockingQueue.put(new PoisonPill());
        }

        executorService.shutdown();
        executorService.awaitTermination(10L, TimeUnit.MINUTES);

        long end = System.currentTimeMillis();

        System.out.println("Elapsed time: " + (end - start) + "ms");
        System.out.println("Elapsed count: " + counts.size());
    }

}
