package com.concurrency.cache;

public class Memorizer {
    private final Computable computable;

    public Memorizer(Computable computable) {
        this.computable = computable;
    }

    public synchronized double compute(double number) {
        return computable.compute(number);
    }
}
