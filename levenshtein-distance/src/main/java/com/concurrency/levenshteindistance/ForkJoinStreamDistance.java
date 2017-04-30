package com.concurrency.levenshteindistance;

import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class ForkJoinStreamDistance implements IDistance {
    private ForkJoinPool threadPool = new ForkJoinPool();

    private final String[] knownWords;

    private final int blockSize;

    public ForkJoinStreamDistance(String[] words, int block) {
        this.knownWords = words;
        this.blockSize = block;
    }

    @Override
    public DistancePair bestMatch(String target) {
        return threadPool.invoke(new DistanceTask(target, 0, knownWords.length, knownWords));
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
    }

    public class DistanceTask extends RecursiveTask<DistancePair> {


        private final String compareText;
        private final String[] matchWords;
        private final int startOffset;
        private final int compareCount;


        public DistanceTask(String target, int offset, int count, String[] words) {
            this.compareText = target;
            this.startOffset = offset;
            this.compareCount = count;
            this.matchWords = words;
        }

        @Override
        protected DistancePair compute() {
            if (compareCount > blockSize) {

                // split range in half and find best result from bests in each half of range
                int half = compareCount / 2;
                DistanceTask t1 = new DistanceTask(compareText, startOffset, half, matchWords);
                t1.fork();

                DistanceTask t2 = new DistanceTask(compareText, startOffset + half, compareCount - half, matchWords);
                DistancePair p2 = t2.compute();

                return DistancePair.best(p2, t1.join());
            }

            // directly compare distances for comparison words in range
            int[] v0 = new int[compareText.length() + 1];
            int[] v1 = new int[compareText.length() + 1];

            return IntStream.range(startOffset, startOffset + compareCount)
                    .mapToObj(i -> new DistancePair(editDistance(compareText, knownWords[i], v0, v1), knownWords[i]))
                    .reduce(DistancePair.worstMatch(), DistancePair::best);
        }

        private int editDistance(String target, String word, int[] v0, int[] v1) {

            // initialize v0 (prior row of distances) as edit distance for empty 'word'
            for (int i = 0; i < v0.length; i++) {
                v0[i] = i;
            }

            // calculate updated v0 (current row distances) from the previous row v0
            for (int i = 0; i < word.length(); i++) {

                // first element of v1 = delete (i+1) chars from target to match empty 'word'
                v1[0] = i + 1;

                // use formula to fill in the rest of the row
                for (int j = 0; j < target.length(); j++) {
                    int cost = (word.charAt(i) == target.charAt(j)) ? 0 : 1;
                    v1[j + 1] = minimum(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
                }

                // swap v1 (current row) and v0 (previous row) for next iteration
                int[] hold = v0;
                v0 = v1;
                v1 = hold;
            }

            // return final value representing best edit distance
            return v0[target.length()];
        }

        private int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }

    }

}
