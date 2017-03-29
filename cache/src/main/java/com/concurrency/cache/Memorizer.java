package com.concurrency.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Memorizer {
    private final Computable computable;
    private final Map<Double, Double> cache = new HashMap<>();

    Memorizer(Computable computable) {
        this.computable = computable;
    }

    // TODO: can not deal with concurrency
    synchronized double compute(double number) {
        Double result = cache.get(number);

        if (Objects.isNull(result)) {
          result = computable.compute(number);
          cache.put(number, result);
        }

        return result;
    }
}
