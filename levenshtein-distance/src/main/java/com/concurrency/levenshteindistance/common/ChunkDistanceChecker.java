package com.concurrency.levenshteindistance.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ChunkDistanceChecker {
	
    private final String[] knownWords;

    public ChunkDistanceChecker(String[] knownWords) {
    	this.knownWords = knownWords;
    }


	public static List<ChunkDistanceChecker> buildCheckers(String[] words, int block) {
		List<ChunkDistanceChecker> checkers = new ArrayList<>();
        for (int base = 0; base < words.length; base += block) {
        	int length = Math.min(block, words.length - base);
        	checkers.add(new ChunkDistanceChecker(Arrays.copyOfRange(words, base, base + length)));
        }
        return checkers;
	}
    
    public DistancePair bestDistance(String target) {
        int[] v0 = new int[target.length() + 1];
        int[] v1 = new int[target.length() + 1];
        int bestIndex = -1;
        int bestDistance = Integer.MAX_VALUE;
        boolean single = false;
        for (int i = 0; i < knownWords.length; i++) {
            int distance = editDistance(target, knownWords[i], v0, v1);
            if (bestDistance > distance) {
                bestDistance = distance;
                bestIndex = i;
                single = true;
            } else if (bestDistance == distance) {
                single = false;
            }
        }
        return single ? new DistancePair(bestDistance, knownWords[bestIndex]) :
        	new DistancePair(bestDistance);
    }

    public DistancePair bestDistanceSimple(String target) {
        int[] v0 = new int[target.length() + 1];
        int[] v1 = new int[target.length() + 1];

        DistancePair best = DistancePair.worstMatch();
        for (String knownWord : knownWords) {
            best = DistancePair.best(best, new DistancePair(editDistance(target, knownWord, v0, v1), knownWord));
        }
        return best;
    }

    public DistancePair bestDistanceStream(String target) {
        int[] v0 = new int[target.length() + 1];
        int[] v1 = new int[target.length() + 1];

        return IntStream.range(0, knownWords.length)
            .mapToObj(i -> new DistancePair(editDistance(target, knownWords[i], v0, v1), knownWords[i]))
            .reduce(DistancePair.worstMatch(), DistancePair::best);
    }

    private int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
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
}