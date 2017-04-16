package com.concurrency.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Memorizer<K, V> {
    private static final  Logger logger = Logger.getLogger("memorizer.logger");

    private final Computable<K, V> computable;
    private final Map<K, Future<V>> cache = new ConcurrentHashMap<>();

    Memorizer(Computable<K, V> computable) {
        this.computable = computable;
    }

    V compute(K key) throws ExecutionException, InterruptedException {
        while (true) {
            Future<V> future = cache.get(key);

            if (Objects.isNull(future)) {
                Callable<V> callable = () -> computable.compute(key);
                FutureTask<V> task = new FutureTask<>(callable);

                future = cache.putIfAbsent(key, task);  // concurrency control

                if (Objects.isNull(future)) {
                    future = task;
                    task.run();
                }
            }

            try {
                return future.get();
            } catch (CancellationException | ExecutionException e) {
                cache.remove(key);

                logger.log(Level.FINE, e.getMessage());
                throw e;
            }
        }
    }
}
