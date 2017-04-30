package com.concurrency.levenshteindistance;

import com.concurrency.levenshteindistance.common.DistancePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FokJoinDistanceTest {

    private ForkJoinDistance forkJoinDistance;

    @Before
    public void setUp() {
        forkJoinDistance = new ForkJoinDistance(TestWords.TEST_WORDS, 100);
    }

    @After
    public void tearDown() throws Exception {
        forkJoinDistance.shutdown();
    }

    @Test
    public void testList() {
        assertEquals(new DistancePair(0, "teachers"), forkJoinDistance.bestMatch("teachers"));
        assertEquals(new DistancePair(1, "teachers"), forkJoinDistance.bestMatch("terchers"));
        assertEquals(new DistancePair(1, "unsuccessful"), forkJoinDistance.bestMatch("unsuccesful"));
        assertEquals(new DistancePair(4, "turntable"), forkJoinDistance.bestMatch("adresable"));
        assertEquals(new DistancePair(2), forkJoinDistance.bestMatch("blovd"));
        assertEquals(new DistancePair(2, "million"), forkJoinDistance.bestMatch("minion"));
        assertEquals(new DistancePair(4, "million"), forkJoinDistance.bestMatch("comision"));
        assertEquals(new DistancePair(1, "defended"), forkJoinDistance.bestMatch("defnded"));
        assertEquals(new DistancePair(1, "valued"), forkJoinDistance.bestMatch("value"));
        assertEquals(new DistancePair(1, "care"), forkJoinDistance.bestMatch("cars"));
        assertEquals(new DistancePair(2, "article's"), forkJoinDistance.bestMatch("articls"));
    }
}
