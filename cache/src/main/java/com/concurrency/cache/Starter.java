package com.concurrency.cache;

import java.math.BigInteger;
import java.util.logging.Logger;

public class Starter {

    private final static Logger logger = Logger.getLogger("cache.logger");

    public static void main(String[] args) {

        Memorizer<String, BigInteger> memorizer = new Memorizer<>(new ExpensiveFunction());

        Runnable runnableWithThreadSleep = () -> {
            try {
                Thread.sleep(100);

                BigInteger compute = memorizer.compute("100");
                logger.info(Thread.currentThread().getName() + ": " + compute);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnableWithOutThreadSleep = () -> {
            BigInteger compute = memorizer.compute("200");
            logger.info(Thread.currentThread().getName() + ": " + compute);
        };

        new Thread(runnableWithThreadSleep, "first thread").start();
        new Thread(runnableWithOutThreadSleep, "second thread").start();
    }
}
