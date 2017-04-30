
package com.concurrency.levenshteindistance.completableFuture;

import com.concurrency.levenshteindistance.IDistance;
import com.concurrency.levenshteindistance.common.ChunkDistanceChecker;
import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureStreamDistance implements IDistance {
    private final List<ChunkDistanceChecker> chunkCheckers;

    private final int blockSize;

    public CompletableFutureStreamDistance(String[] words, int block) {
        this.blockSize = block;
        this.chunkCheckers = ChunkDistanceChecker.buildCheckers(words, block);
    }

    @Override
    public DistancePair bestMatch(String target) {

        return chunkCheckers.stream()
                .map(checker -> CompletableFuture.supplyAsync(() -> checker.bestDistance(target)))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .reduce(DistancePair.worstMatch(), DistancePair::best);
    }



    @Override
    public void shutdown() {
    }
}