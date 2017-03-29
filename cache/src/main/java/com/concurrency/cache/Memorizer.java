package com.concurrency.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

class Memorizer {
    private final Computable computable;
    private final Map<Double, Double> cache = new ConcurrentHashMap<>();

    Memorizer(Computable computable) {
        this.computable = computable;
    }

    double compute(double number) {
        Double result = cache.get(number);

        if (Objects.isNull(result)) {
          result = computable.compute(number);
          cache.put(number, result);
        }

        return result;
    }
}
