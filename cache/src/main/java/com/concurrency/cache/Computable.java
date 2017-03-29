package com.concurrency.cache;

public class Computable {
    public double basicNumber = 100;

    public double compute(double number) {
        basicNumber = basicNumber * number;
        return basicNumber;
    }
}
