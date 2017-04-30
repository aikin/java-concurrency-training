package com.concurrency.levenshteindistance;


import com.concurrency.levenshteindistance.common.DistancePair;

import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Run timed test of finding best matches for misspelled words.
 */
public class NonChunkParallelDistance implements IDistance {
    private final String[] knownWords;

    public NonChunkParallelDistance(String[] words) {
        this.knownWords = words;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public DistancePair bestMatch(String target) {

        Supplier<WordChecker> supplier = () -> new WordChecker(target.length() + 1);
        ObjIntConsumer<WordChecker> accumulator = (t, value) -> t.checkWord(target, knownWords[value]);
        BiConsumer<WordChecker, WordChecker> combiner = WordChecker::merge;

        return IntStream.range(0, knownWords.length)
                .parallel()
                .collect(supplier, accumulator, combiner)
                .result();
    }

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private static int editDistance(String target, String known, int[] v0, int[] v1) {

        // initialize v0 (prior row of distances) as edit distance for empty 'word'
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        // calculate updated v0 (current row distances) from the previous row v0
        for (int i = 0; i < known.length(); i++) {

            // first element of v1 = delete (i+1) chars from target to match empty 'word'
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < target.length(); j++) {
                int cost = (known.charAt(i) == target.charAt(j)) ? 0 : 1;
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

    private static class WordChecker {
        private final int[] v0;
        private final int[] v1;

        private int bestDistance = Integer.MAX_VALUE;
        private String bestKnown = "NONE";

        public WordChecker(int length) {
            v0 = new int[length];
            v1 = new int[length];
        }

        private void checkWord(String target, String known) {
            int distance = editDistance(target, known, v0, v1);

            if (bestDistance > distance) {
                bestDistance = distance;
                bestKnown = known;
            } else if (bestDistance == distance) {
                bestKnown = null;
            }
        }

        private void merge(WordChecker other) {
            if (bestDistance > other.bestDistance) {
                bestDistance = other.bestDistance;
                bestKnown = other.bestKnown;
            } else if (bestDistance == other.bestDistance) {
                bestKnown = null;
            }
        }

        private DistancePair result() {
            return (bestKnown == null) ? new DistancePair(bestDistance) : new DistancePair(bestDistance, bestKnown);
        }
    }
}