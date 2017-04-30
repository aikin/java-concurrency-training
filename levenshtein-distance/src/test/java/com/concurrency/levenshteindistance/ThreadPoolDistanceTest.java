package com.concurrency.levenshteindistance;

import com.concurrency.levenshteindistance.common.DistancePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThreadPoolDistanceTest {


   private ThreadPoolDistance threadPoolDistance;

   @Before
   public void setUp() {
      threadPoolDistance = new ThreadPoolDistance(TestWords.TEST_WORDS, 100);
   }

   @After
   public void tearDown() throws Exception {

   }

   @Test
    public void should_match_correct_distance_when_input_different_target() {


       assertEquals(new DistancePair(0, "teachers"), threadPoolDistance.bestMatch("teachers"));
       assertEquals(new DistancePair(1, "teachers"), threadPoolDistance.bestMatch("terchers"));
       assertEquals(new DistancePair(1, "unsuccessful"), threadPoolDistance.bestMatch("unsuccesful"));
       assertEquals(new DistancePair(4, "turntable"), threadPoolDistance.bestMatch("adresable"));
       assertEquals(new DistancePair(2), threadPoolDistance.bestMatch("blovd"));
       assertEquals(new DistancePair(2, "million"), threadPoolDistance.bestMatch("minion"));
       assertEquals(new DistancePair(4, "million"), threadPoolDistance.bestMatch("comision"));
       assertEquals(new DistancePair(1, "defended"), threadPoolDistance.bestMatch("defnded"));
       assertEquals(new DistancePair(1, "valued"), threadPoolDistance.bestMatch("value"));
       assertEquals(new DistancePair(1, "care"), threadPoolDistance.bestMatch("cars"));
       assertEquals(new DistancePair(2, "article's"), threadPoolDistance.bestMatch("articls"));
   }
}
