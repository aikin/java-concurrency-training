
package com.concurrency.levenshteindistance;

import com.concurrency.levenshteindistance.common.ChunkDistanceChecker;
import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class CompletableFutureWithCompletionHandlerDistance implements IDistance {
    private final List<ChunkDistanceChecker> chunkCheckers;

    private final int blockSize;

    public CompletableFutureWithCompletionHandlerDistance(String[] words, int block) {
        this.blockSize = block;
        this.chunkCheckers = ChunkDistanceChecker.buildCheckers(words, block);
    }

    @Override
    public DistancePair bestMatch(String target) {

        AtomicReference<DistancePair> best = new AtomicReference<>(DistancePair.worstMatch());
        CountDownLatch countDownLatch = new CountDownLatch(chunkCheckers.size());


        for (ChunkDistanceChecker checker : chunkCheckers) {

            CompletableFuture
                    .supplyAsync(() -> checker.bestDistance(target))
                    .thenAccept(result -> {
                        best.accumulateAndGet(result, DistancePair::best);
                        countDownLatch.countDown();
                    });

        }

        try {

            countDownLatch.await();

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted during calculations", e);
        }

        return best.get();
    }


    @Override
    public void shutdown() {
    }
}