package com.concurrency.levenshteindistance;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DistanceTest {

    private static final String[] TEST_WORDS = "accepts:access's:accord:aiding:amazing:approximately:article's:aspects:assignments:asynchronous:avoiding:beloved:boiled:burning:candidate:care:cartridge's:chain:coincide:comparison's:competing:concern's:cooked:counting:crash's:dare:debug:declares:deeply:defended:descends:dictionary:dole:drawn:elections:end's:epic:equivalents:everything:expiry:external:factory:far:founding:gasp:glove:grind:head's:high:hurts:distanceausible:incoming:intake:intends:lecturer:logically:lost:man:mentally:meters:million:miracle's:misunderstanding:nations:network:often:overpricing:overtime:painting:pardon:piano:pit:prevention:pursues:quotation's:re:restrict:risk's:scientist's:settle:settling:several:sights:slang:starters:steered:stolen:store's:supervisions:sympathize:tea:teachers:thus:tower:transaction's:turntable:unsuccessful:valued:very:voluntarily".split(":");
    private Distance distance;

    @Before
    public void setUp() throws Exception {
        distance = new Distance(TEST_WORDS);
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