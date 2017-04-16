package com.concurrency.cache;

import java.math.BigInteger;
import java.util.logging.Logger;

public class Starter {

    private static final Logger logger = Logger.getLogger("cache.logger");

    private Starter() {
        throw new IllegalAccessError("Starter class");
    }

    public static void main(String[] args) {

        Memorizer<String, BigInteger> memorizer = new Memorizer<>(new ExpensiveFunction());

        Runnable runnableWithThreadSleep = () -> {
            try {
                Thread.sleep(100);

                BigInteger compute = memorizer.compute("100");
                logger.info(Thread.currentThread().getName() + ": " + compute);
            } catch (Exception e) {
                logger.info(String.valueOf(e));
            }
        };

        Runnable runnableWithOutThreadSleep = () -> {
            BigInteger compute = null;
            try {
                compute = memorizer.compute("200");
            } catch (Exception e) {
                logger.info(String.valueOf(e));
            }
            logger.info(Thread.currentThread().getName() + ": " + compute);
        };

        new Thread(runnableWithThreadSleep, "first thread").start();
        new Thread(runnableWithOutThreadSleep, "second thread").start();
    }
}
