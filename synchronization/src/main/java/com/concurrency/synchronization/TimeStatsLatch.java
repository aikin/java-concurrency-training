package com.concurrency.synchronization;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;


/**
 * 模拟真实并发应用程序，并进行执行时间性能统计。
 *
 * <p>测试N个线程并发执行某个任务时需要的时间。
 */

class TimeStatsLatch {

    static long timeTasks(int nThreads, final Runnable task) throws InterruptedException {

        //TODO: refactor: code not in same abstract level

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

        createMultiThread(nThreads, runnable);

        long startTime = System.currentTimeMillis();

        startGate.countDown();
        endGate.await();

        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    private static void createMultiThread(int nThreads, Runnable runnable) {
        IntStream.range(0, nThreads).forEach(
                i -> {
                    Thread t = new Thread(runnable);
                    t.start();
                }
        );
    }
}
