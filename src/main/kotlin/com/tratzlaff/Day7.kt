package com.tratzlaff

fun main() {
    val day = Day7()
    println("Part 1: "+day.solvePart1(readLines("day7.txt")))
    println("Part 2: "+day.solvePart2(readLines("day7.txt")))
}

// https://adventofcode.com/2023/day/7
class Day7 {
    fun solvePart1(lines: List<String>): Long {
        var winnings = 0
        lines.map { Hand(it) }.sorted().forEachIndexed { index, hand ->
           winnings += hand.bid * (index + 1)
        }
        return winnings.toLong()
    }

    fun solvePart2(lines: List<String>): Long {
        var winnings = 0
        lines.map { Hand2(it) }.sorted().forEachIndexed { index, hand ->
            winnings += hand.bid * (index + 1)
        }
        return winnings.toLong()
    }
}

data class Hand2(val line: String): Comparable<Hand2> {
    val cards: String
    val cardToCount: Map<Char, Int>
    val bid: Int
    val jokerCount: Int
    val maxOfAKind: Int
    val countPairs: Int
    init {
        line.trim().split(" ").let {
            bid = it.last().toInt()
            cards = it.first()
            cardToCount = cards.groupBy { it }.mapValues { it.value.size }
            jokerCount = cardToCount['J'] ?: 0
            maxOfAKind = (cardToCount - 'J').values.maxOrNull()?.let { it + jokerCount } ?: jokerCount
            countPairs = cardToCount.countPairs()
        }
    }

    private fun Map<Char, Int>.countPairs(): Int {
        // Count the pairs that do not involve Jokers.
        val nonJokerPairs = this.filter { it.key != 'J' && it.value == 2 }.size

        // Count the single cards that can potentially form pairs with Jokers.
        val singleCards = this.filter { it.key != 'J' && it.value == 1 }.size

        // The total number of pairs is the sum of non-Joker pairs and the number of single cards that can be paired with Jokers.
        // If there are more single cards than Jokers, only a number of pairs equal to the number of Jokers can be formed.
        // If there are more Jokers than single cards, all single cards can form pairs with Jokers.
        return nonJokerPairs + minOf(jokerCount, singleCards)
    }

    fun isFiveOfAKind() = maxOfAKind == 5
    fun isFourOfAKind() = maxOfAKind == 4
    fun isFullHouse() = (cardToCount - 'J').size == 2 && maxOfAKind == 3
    fun isThreeOfAKind() = maxOfAKind == 3
    fun isTwoPair() = countPairs == 2
    fun isOnePair() = countPairs == 1

    fun getOrderValue() = when {
        isFiveOfAKind() -> 7
        isFourOfAKind() -> 6
        isFullHouse() -> 5
        isThreeOfAKind() -> 4
        isTwoPair() -> 3
        isOnePair() -> 2
        else -> 1
    }

    private val customComparator = Comparator<String> { str1, str2 ->
        val order = "AKQT98765432J"
        for (i in 0 .. 4) {
            val index1 = order.indexOf(str1[i])
            val index2 = order.indexOf(str2[i])
            if (index1 != index2) return@Comparator index1 - index2
        }
        0
    }

    override fun compareTo(other: Hand2): Int {
        return if(getOrderValue() == other.getOrderValue()) {
            customComparator.compare(cards, other.cards) * -1
        } else {
            getOrderValue().compareTo(other.getOrderValue())
        }
    }
}

data class Hand(val line: String): Comparable<Hand> {
    val cardToCount: Map<Char, Int>
    val cards: String
    val bid: Int
    init {
        line.trim().split(" ").let {
            cards = it.first()
            cardToCount = cards.groupBy { it }.mapValues { it.value.size }
            bid = it.last().toInt()
        }
    }

    fun isFiveOfAKind() = cardToCount.any { it.value == 5 }
    fun isFourOfAKind() = cardToCount.any { it.value == 4 }
    fun isFullHouse() = isThreeOfAKind() && isOnePair()
    fun isThreeOfAKind() = cardToCount.any { it.value == 3 }
    fun isTwoPair() = cardToCount.filter { it.value == 2 }.size == 2
    fun isOnePair() = cardToCount.filter { it.value == 2 }.size == 1
    fun getOrderValue() = when {
        isFiveOfAKind() -> 7
        isFourOfAKind() -> 6
        isFullHouse() -> 5
        isThreeOfAKind() -> 4
        isTwoPair() -> 3
        isOnePair() -> 2
        else -> 1
    }

    private val customComparator = Comparator<String> { str1, str2 ->
        val order = "AKQJT98765432"
        for (i in 0 .. 4) {
            val index1 = order.indexOf(str1[i])
            val index2 = order.indexOf(str2[i])
            if (index1 != index2) return@Comparator index1 - index2
        }
        0
    }

    override fun compareTo(other: Hand): Int {
        return if(getOrderValue() == other.getOrderValue()) {
            customComparator.compare(cards, other.cards) * -1
        } else {
            getOrderValue().compareTo(other.getOrderValue())
        }
    }
}