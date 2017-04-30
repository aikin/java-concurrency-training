
package com.concurrency.levenshteindistance.completableFuture;

import com.concurrency.levenshteindistance.IDistance;
import com.concurrency.levenshteindistance.common.ChunkDistanceChecker;
import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDistance implements IDistance {
    private final List<ChunkDistanceChecker> chunkCheckers;

    private final int blockSize;

    public CompletableFutureDistance(String[] words, int block) {
        this.blockSize = block;
        this.chunkCheckers = ChunkDistanceChecker.buildCheckers(words, block);
    }

    @Override
    public DistancePair bestMatch(String target) {
        List<CompletableFuture<DistancePair>> futures = new ArrayList<>();

        for (ChunkDistanceChecker checker: chunkCheckers) {
            CompletableFuture<DistancePair> future = CompletableFuture.supplyAsync(() -> checker.bestDistance(target));
            futures.add(future);
        }

        DistancePair best = DistancePair.worstMatch();
        for (CompletableFuture<DistancePair> future: futures) {
            best = DistancePair.best(best, future.join());
        }
        return best;
    }


    @Override
    public void shutdown() {
    }
}