package com.tratzlaff

// https://adventofcode.com/2023/day/9
fun main() {
    println("Part 1: "+solvePart1(readLines("day9.txt")))
    println("Part 2: "+solvePart2(readLines("day9.txt")))
}

fun solvePart1(lines: List<String>): Long {
    val report = lines.map { it.trim().split(" ").map { it.toLong() } }
    return report.sumOf {
        var below = 0L
        it.generateDiffLists().forEach { below += it.last() }
        it.last() + below
    }
}

fun solvePart2(lines: List<String>): Long {
    val report = lines.map { it.trim().split(" ").map { it.toLong() } }
    return report.sumOf {
        var below = 0L
        it.generateDiffLists().forEach { below = it.first()-below }
        it.first() - below
    }
}

fun List<Long>.generateDiffLists(): List<List<Long>> {
    val diffLists = mutableListOf<List<Long>>()
    diffLists.add(this.generateDiffList())
    while(diffLists.last().any { it != 0L }) {
        diffLists.last().generateDiffList().let { diffLists.add(it) }
    }
    diffLists.removeLast()
    diffLists.reverse()
    return diffLists
}

fun List<Long>.generateDiffList(): List<Long> {
    val diffList = mutableListOf<Long>()
    for (i in 0 until this.size - 1) {
        diffList.add(this[i + 1] - this[i])
    }
    return diffList
}