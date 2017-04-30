
package com.concurrency.levenshteindistance.completableFuture;

import com.concurrency.levenshteindistance.IDistance;
import com.concurrency.levenshteindistance.common.ChunkDistanceChecker;
import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWithCombineDistance implements IDistance {
    private final List<ChunkDistanceChecker> chunkCheckers;

    private final int blockSize;

    public CompletableFutureWithCombineDistance(String[] words, int block) {
        this.blockSize = block;
        this.chunkCheckers = ChunkDistanceChecker.buildCheckers(words, block);
    }

    @Override
    public DistancePair bestMatch(String target) {
        CompletableFuture<DistancePair> last = CompletableFuture.supplyAsync(bestDistanceLambda(0, target));

        for (int i = 1; i < chunkCheckers.size(); i++) {
            last = CompletableFuture.supplyAsync(bestDistanceLambda(i, target))
                    .thenCombine(last, DistancePair::best);
        }
        return last.join();
    }

    private Supplier<DistancePair> bestDistanceLambda(int i, String target) {
        return () -> chunkCheckers.get(i).bestDistance(target);
    }


    @Override
    public void shutdown() {
    }
}