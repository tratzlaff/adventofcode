package com.tratzlaff

import java.util.stream.Stream
import kotlin.streams.asStream

fun main() {
    val day = Day5()
    println("Part 1: "+day.solvePart1(readLines("day5.txt")))
    println("Part 1: "+day.solvePart2(readLines("day5.txt")))
}

// https://adventofcode.com/2023/day/5
class Day5 {
    // destination range start, source range start, range length
    fun solvePart1(lines: List<String>): Long {
        val seeds = mutableListOf<Long>()
        val maps = mutableListOf<Category>()
        val mappings = mutableListOf<Mapping>()

        lines.forEach {
            if(it.trim().startsWith("seeds:")) {
                seeds.addAll(it.substringAfter(":").trim().split(" ").map { it.trim().toLong() })
            } else if(it.isBlank()) {
                if(mappings.isNotEmpty()) {
                    maps.add(Category(mappings.toList()))
                }
            } else if(it.trim().first().isLetter()) {
                mappings.clear()
            } else {
                val numbers = it.trim().split(" ").map { it.toLong() }
                mappings.add(Mapping(numbers[0], numbers[1], numbers[2]))
            }
        }
        maps.add(Category(mappings.toList()))

        return seeds.minOfOrNull { maps.getLocation(it) }!!
    }

    fun solvePart2(lines: List<String>): Long {
        var seeds: Seeds? = null
        val maps = mutableListOf<Category>()
        val mappings = mutableListOf<Mapping>()

        lines.forEach {
            if(it.trim().startsWith("seeds:")) {
                seeds = Seeds(it.substringAfter(":").trim())
            } else if(it.isBlank()) {
                if(mappings.isNotEmpty()) {
                    maps.add(Category(mappings.toList()))
                }
            } else if(it.trim().first().isLetter()) {
                mappings.clear()
            } else {
                val numbers = it.trim().split(" ").map { it.toLong() }
                mappings.add(Mapping(numbers[0], numbers[1], numbers[2]))
            }
        }
        maps.add(Category(mappings.toList()))

        return seeds!!.getAllValues()
            .parallel()
            .map { maps.getLocation(it) }
            .min(Comparator.naturalOrder())
            .orElseThrow()
    }

    private fun List<Category>.getLocation(seed: Long): Long {
        var num = seed
        this.forEach { num = it.getMappedValue(num) }
        return num
    }

    data class Mapping(val destinationStart: Long, val sourceStart: Long, val rangeLength: Long) {
        fun getMappedValue(input: Long): Long {
            if (input in sourceStart until sourceStart + rangeLength) {
                return destinationStart + (input - sourceStart)
            }
            return input
        }
    }

    data class Category(val mappings: List<Mapping>) {
        fun getMappedValue(input: Long): Long {
            for (mapping in mappings) {
                val newResult = mapping.getMappedValue(input)
                if (newResult != input) {
                    return newResult
                }
            }
            return input
        }
    }

    data class Seeds(val seeds: String) {
        private val rangeList: List<LongRange> = seeds.split(" ")
            .map { it.trim().toLong() }
            .windowed(2, 2)
            .map { it[0] until it[0]+it[1] }

        fun getAllValues(): Stream<Long> {
            return rangeList.asSequence().flatMap { it.asSequence() }.asStream()
        }
    }
}