package com.concurrency.levenshteindistance;

public interface IDistance {
    DistancePair bestMatch(String targetText);
    void shutdown();
}
