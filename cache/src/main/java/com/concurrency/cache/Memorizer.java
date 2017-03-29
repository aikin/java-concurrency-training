package com.concurrency.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

class Memorizer<K, V> {
    private final Computable<K, V> computable;
    private final Map<K, V> cache = new ConcurrentHashMap<>();

    Memorizer(Computable<K, V> computable) {
        this.computable = computable;
    }

    V compute(K key) {
        V result = cache.get(key);

        if (Objects.isNull(result)) {
          result = computable.compute(key);
          cache.put(key, result);
        }

        return result;
    }
}
