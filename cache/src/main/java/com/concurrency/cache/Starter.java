package com.concurrency.cache;

import java.util.logging.Logger;

public class Starter {

    private final static Logger logger = Logger.getLogger("cache.logger");

    public static void main(String[] args) {
        Memorizer memorizer = new Memorizer(new Computable());

        Runnable runnableWithThreadSleep = () -> {
            try {
                Thread.sleep(100);
                double compute = memorizer.compute(100);
                logger.info(Thread.currentThread().getName() + ": " + compute);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnableWithOutThreadSleep = () -> {
            double compute = memorizer.compute(100);
            logger.info(Thread.currentThread().getName() + ": " + compute);
        };

        new Thread(runnableWithThreadSleep, "first thread").start();
        new Thread(runnableWithOutThreadSleep, "second thread").start();
    }
}
