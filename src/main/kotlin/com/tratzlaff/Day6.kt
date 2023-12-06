package com.tratzlaff

fun main() {
    val day = Day6()

    val input1 = mapOf(48L to 296L, 93L to 1928L, 85L to 1236L, 95L to 1391L)
    println("Part 1: "+day.getWaysToWin(input1).reduce(Int::times))

    val input2 = mapOf(48938595L to 296192812361391)
    println("Part 2: "+day.getWaysToWin(input2).first())
}

// https://adventofcode.com/2023/day/6
class Day6 {
    /**
     * @param input map from time to distance for each race
     * @return number of ways you can beat given distance for each race in the given time
     */
    fun getWaysToWin(input: Map<Long, Long>) = input.map { (raceTime, recordDistance) ->
        (0 until raceTime).count { calculateDistance(it, raceTime) > recordDistance }
    }

    private fun calculateDistance(holdTime: Long, raceTime: Long) = holdTime * (raceTime - holdTime)
}