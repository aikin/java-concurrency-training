
package com.concurrency.levenshteindistance;

import com.concurrency.levenshteindistance.common.ChunkDistanceChecker;
import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.List;

public class ChunkParallelDistance implements IDistance {
    private final List<ChunkDistanceChecker> chunkCheckers;

    private final int blockSize;

    public ChunkParallelDistance(String[] words, int block) {
        this.blockSize = block;
        this.chunkCheckers = ChunkDistanceChecker.buildCheckers(words, block);
    }

    @Override
    public DistancePair bestMatch(String target) {
        return chunkCheckers.parallelStream()
                .map(checker -> checker.bestDistanceSimple(target))
                .reduce(DistancePair.worstMatch(), DistancePair::best);
    }



    @Override
    public void shutdown() {
    }
}