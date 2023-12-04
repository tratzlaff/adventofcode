package com.tratzlaff

fun main() {
    val day = Day4()
    println("Part 1: "+day.solvePart1(readLines("day4.1.txt")))
    println("Part 2: "+day.solvePart2(readLines("day4.2.txt")))
}

// https://adventofcode.com/2023/day/4
class Day4 {
    fun solvePart1(lines: List<String>): Int {
        return lines.map { Card(it) }.sumOf { it.points }
    }

    fun solvePart2(lines: List<String>): Int {
        var count = 0;
        val numToCard = lines.map { Card(it) }.associateBy { it.num }
        val queue = ArrayDeque(numToCard.values)
        while(queue.isNotEmpty()) {
            val card = queue.removeFirst()
            count++

            for(i in 1..card.winCount) {
                queue.add(numToCard[card.num+i]!!)
            }
        }

        return count
    }

    data class Card(val line: String) {
        val num: Int
        val winningNumbers: List<Int>
        val yourNumbers: List<Int>
        var points: Int = 0
        var winCount: Int = 0

        init {
            line.split(":").let {
                num = it[0].trim().substringAfter(" ").trim().toInt()
                it[1].trim().split("|").let { numbersSplit ->
                    winningNumbers = numbersSplit[0].trim().split(" ").filter { it.isNotEmpty() }.map { wn -> wn.toInt() }
                    yourNumbers = numbersSplit[1].trim().split(" ").filter { it.isNotEmpty() }.map { yn -> yn.toInt() }
                }
            }

            yourNumbers.forEach {
                if (winningNumbers.contains(it)) {
                    winCount++
                    if(points == 0) {
                        points = 1
                    } else {
                        points *= 2
                    }
                }
            }
        }
    }

}