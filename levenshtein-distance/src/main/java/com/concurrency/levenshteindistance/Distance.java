package com.concurrency.levenshteindistance;

public class Distance {

    private final String[] knownWords;

    public Distance(String[] knownWords) {
        this.knownWords = knownWords;
    }

    public DistancePair bestMatch(String target) {
        return new DistancePair(0, target);
    }
}
