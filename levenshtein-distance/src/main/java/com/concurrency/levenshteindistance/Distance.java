package com.concurrency.levenshteindistance;

public class Distance implements IDistance {

    private final String[] knownWords;

    public Distance(String[] knownWords) {
        this.knownWords = knownWords;
    }

    @Override
    public DistancePair bestMatch(String targetText) {
        int[] v0 = new int[targetText.length() + 1];
        int[] v1 = new int[targetText.length() + 1];

        int bestIndex = -1;
        int bestDistance = Integer.MAX_VALUE;
        boolean single = false;


        for (int i = 0; i < knownWords.length; i++) {
            int distance = editDistance(targetText, knownWords[i], v0, v1);
            if (bestDistance > distance) {
                bestDistance = distance;
                bestIndex = i;
                single = true;
            } else if (bestDistance == distance) {
                single = false;
            }
        }


        return single ? new DistancePair(bestDistance, knownWords[bestIndex]) :
                new DistancePair(Integer.MAX_VALUE);
    }

    @Override
    public void shutdown() {

    }

    private int editDistance(String targetText, String word, int[] v0, int[] v1) {

        // initialize v0 (prior row of distances) as edit distance for empty 'word'
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        // calculate updated v0 (current row distances) from the previous row v0
        for (int i = 0; i < word.length(); i++) {

            // first element of v1 = delete (i+1) chars from target to match empty 'word'
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < targetText.length(); j++) {
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
