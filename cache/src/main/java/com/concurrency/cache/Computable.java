package com.concurrency.cache;

public interface Computable<K, V> {
    V compute(K key);
}
