package com.concurrency.levenshteindistance;

import com.concurrency.levenshteindistance.common.DistancePair;

public interface IDistance {
    DistancePair bestMatch(String targetText);
    void shutdown();
}
