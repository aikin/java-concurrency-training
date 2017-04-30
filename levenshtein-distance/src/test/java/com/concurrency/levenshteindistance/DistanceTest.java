package com.concurrency.levenshteindistance;


import com.concurrency.levenshteindistance.common.DistancePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DistanceTest {

    private Distance distance;

    @Before
    public void setUp() throws Exception {
        distance = new Distance(TestWords.TEST_WORDS);
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void should_match_correct_distance_when_input_different_target() {

        assertEquals(new DistancePair(0, "teachers"), distance.bestMatch("teachers"));
        assertEquals(new DistancePair(1, "teachers"), distance.bestMatch("terchers"));
        assertEquals(new DistancePair(1, "unsuccessful"), distance.bestMatch("unsuccesful"));
        assertEquals(new DistancePair(4, "turntable"), distance.bestMatch("adresable"));
        assertEquals(new DistancePair(Integer.MAX_VALUE, "NONE"), distance.bestMatch("blovd"));
        assertEquals(new DistancePair(2, "million"), distance.bestMatch("minion"));
        assertEquals(new DistancePair(4, "million"), distance.bestMatch("comision"));
        assertEquals(new DistancePair(1, "defended"), distance.bestMatch("defnded"));
        assertEquals(new DistancePair(1, "valued"), distance.bestMatch("value"));
        assertEquals(new DistancePair(1, "care"), distance.bestMatch("cars"));
        assertEquals(new DistancePair(2, "article's"), distance.bestMatch("articls"));
    }
}