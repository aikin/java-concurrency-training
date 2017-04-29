package com.concurrency.levenshteindistance;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceTest {

    private static final String[] TEST_WORDS = "accepts:access's:accord:aiding:amazing:approximately:article's:aspects:assignments:asynchronous:avoiding:beloved:boiled:burning:candidate:care:cartridge's:chain:coincide:comparison's:competing:concern's:cooked:counting:crash's:dare:debug:declares:deeply:defended:descends:dictionary:dole:drawn:elections:end's:epic:equivalents:everything:expiry:external:factory:far:founding:gasp:glove:grind:head's:high:hurts:implausible:incoming:intake:intends:lecturer:logically:lost:man:mentally:meters:million:miracle's:misunderstanding:nations:network:often:overpricing:overtime:painting:pardon:piano:pit:prevention:pursues:quotation's:re:restrict:risk's:scientist's:settle:settling:several:sights:slang:starters:steered:stolen:store's:supervisions:sympathize:tea:teachers:thus:tower:transaction's:turntable:unsuccessful:valued:very:voluntarily".split(":");

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void should_return_distance_0_when_target_word_is_teachers() {
        Distance distance = new Distance(TEST_WORDS);

        assertEquals(new DistancePair(0, "teachers"), distance.bestMatch("teachers"));
    }

}