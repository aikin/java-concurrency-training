package com.concurrency.synchronization;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class TimeStatsLatch {

    public static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {

        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        Runnable runnable = () -> {
            try {
                startGate.await();
                try {
                    task.run();
                } finally {
                    endGate.countDown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        IntStream.range(0, nThreads).forEach(
                i -> {
                    Thread t = new Thread(runnable);
                    t.start();
                }
        );

        long startTime = System.currentTimeMillis();

        startGate.countDown();
        endGate.await();

        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }
}
