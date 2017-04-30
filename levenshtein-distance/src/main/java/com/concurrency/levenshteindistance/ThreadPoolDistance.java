package com.concurrency.levenshteindistance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolDistance implements IDistance {

    private final ExecutorService threadPool;

    private final String[] knownWords;

    private final int blockSize;


    public ThreadPoolDistance(String[] knownWords, int blockSize) {
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.knownWords = knownWords;
        this.blockSize = blockSize;
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
    }

    @Override
    public DistancePair bestMatch(String targetText) {

        int size;
        List<DistanceTask> tasks = new ArrayList<>();

        for (int base = 0; base < knownWords.length; base += size) {
            size = Math.min(blockSize, knownWords.length - base);
            tasks.add(new DistanceTask(targetText, base, size));
        }

        DistancePair best = DistancePair.worstMatch();
        try {

            List<Future<DistancePair>> futures = threadPool.invokeAll(tasks);
            for (Future<DistancePair> future : futures) {
                best = DistancePair.best(best, future.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return best;

    }

    private class DistanceTask implements Callable<DistancePair> {
        private final String targetText;
        private final int startOffset;
        private final int compareCount;

        public DistanceTask(String targetText, int startOffset, int compareCount) {
            this.targetText = targetText;
            this.startOffset = startOffset;
            this.compareCount = compareCount;
        }

        @Override
        public DistancePair call() throws Exception {

            int[] v0 = new int[targetText.length() + 1];
            int[] v1 = new int[targetText.length() + 1];

            int bestIndex = -1;
            int bestDistance = Integer.MAX_VALUE;

            boolean single = false;
            for (int i = 0; i < compareCount; i++) {
                int distance = editDistance(targetText, knownWords[i + startOffset], v0, v1);
                if (bestDistance > distance) {
                    bestDistance = distance;
                    bestIndex = i + startOffset;
                    single = true;
                } else if (bestDistance == distance) {
                    single = false;
                }
            }

            return single ? new DistancePair(bestDistance, knownWords[bestIndex]) :
                    new DistancePair(bestDistance);
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
                    int cost = (word.charAt(i) == targetText.charAt(j)) ? 0 : 1;
                    v1[j + 1] = minimum(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
                }

                // swap v1 (current row) and v0 (previous row) for next iteration
                int[] hold = v0;
                v0 = v1;
                v1 = hold;
            }

            // return final value representing best edit distance
            return v0[targetText.length()];
        }

        private int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }
    }
}
