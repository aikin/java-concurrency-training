package com.concurrency.synchronization;

public class App {
    public static void main(String[] args) throws InterruptedException {

        Runnable runnable = () -> {
            System.out.println("runnable" + Thread.currentThread().getName());
        };
        long totalTime = TimeStatsLatch.timeTasks(3, runnable);

        System.out.println("Total threads exec time: " + totalTime);
    }
}
