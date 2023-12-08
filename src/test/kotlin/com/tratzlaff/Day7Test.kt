package com.tratzlaff

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day7Test {
    @Test
    fun test() {
        assertFalse(Hand2("AQ987 0").isOnePair())
        assertTrue( Hand2("AQ98A 0").isOnePair())
        assertFalse(Hand2("QQ9AA 0").isOnePair()) // two pair, not one pair
        assertFalse(Hand2("J89AA 0").isOnePair()) // three of a kind, not one pair

        assertFalse(Hand2("AQ987 0").isTwoPair())
        assertFalse(Hand2("AQ98A 0").isTwoPair())
        assertFalse(Hand2("AQ9AA 0").isTwoPair()) // three of a kind, not two pair
        assertTrue( Hand2("QQ9AA 0").isTwoPair())

        assertTrue( Hand2("JJ9AA 0").isFourOfAKind())
        assertEquals(6, Hand2("JJ9AA 0").getOrderValue())
        assertTrue( Hand2("JJ9QA 0").isThreeOfAKind())

        assertTrue( Hand2("JJJJA 0").isFiveOfAKind())
        assertFalse(Hand2("JJJJA 0").isFourOfAKind())

        assertTrue(Hand2("AQ987 0").compareTo(Hand2("AQ987 0")) == 0)
        assertTrue(Hand2("AQ97J 0") < Hand2("AQ977 0"))
        assertTrue(Hand2("AJJJJ 0") > Hand2("JJJJA 0"))
        assertTrue(Hand2("AJJJJ 0") < Hand2("AAAAA 0"))

        assertTrue(Hand2("AAAQQ 0").isFullHouse())
        assertFalse(Hand2("AAAJJ 0").isFullHouse())
        assertTrue(Hand2("AAAJJ 0").isFiveOfAKind())

        assertFalse(Hand2("AAJ99 0").isFiveOfAKind())
        assertFalse(Hand2("AAJ99 0").isFourOfAKind())
        assertFalse(Hand2("AAJ99 0").isOnePair())
        assertTrue(Hand2("AAJ99 0").isFullHouse())
    }
}