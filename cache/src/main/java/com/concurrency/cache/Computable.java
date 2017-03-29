package com.concurrency.cache;

class Computable {
    private double basicNumber = 100;

    double compute(double number) {
        basicNumber = basicNumber * number;
        return basicNumber;
    }
}
