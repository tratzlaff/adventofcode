package com.tratzlaff

fun main() {
    val day = Day3()
    val input = readText("day3.txt")
    println("Part 1: "+day.solvePart1(input)) // 553825
    println("Part 2: "+day.solvePart2(input)) // 93994191
}

// https://adventofcode.com/2023/day/3
class Day3 {
    private val symbols = "*#+$%/&!@=<>?^|~-"

    fun solvePart1(input: String): Int {
        val schematic = input.lines().map { it.processRow() }

        fun getNeighborIndices(rowIndex: Int): Set<Int> {
            val indices = mutableSetOf<Int>()
            if(rowIndex > 0) {
                indices.addAll(schematic[rowIndex-1].symbolIndices)
            }
            if(rowIndex < schematic.size-1) {
                indices.addAll(schematic[rowIndex+1].symbolIndices)
            }
            return indices
        }

        var result = 0
        schematic.forEachIndexed { rowIndex, row ->
            val neighborIndices = getNeighborIndices(rowIndex)
            row.getValidPartNumbers(neighborIndices).forEach {
                result += it
            }
        }

        return result
    }

    fun solvePart2(input: String): Int {
        val schematic = input.lines().map { it.processRow() }

        fun getAdjacentPartNumbers(rowIndex: Int, gearIndex: Int): List<Int> {
            val partNumbers = mutableListOf<Int>()
            partNumbers.addAll(schematic[rowIndex].getAdjacentPartNumbers(gearIndex))
            if(rowIndex > 0) {
                partNumbers.addAll(schematic[rowIndex-1].getAdjacentPartNumbers(gearIndex))
            }
            if(rowIndex < schematic.size-1) {
                partNumbers.addAll(schematic[rowIndex+1].getAdjacentPartNumbers(gearIndex))
            }
            return partNumbers
        }

        var result = 0
        schematic.forEachIndexed { rowIndex, row ->
            row.gearIndices.forEach { gearIndex ->
                val adjacentPartNumbers = getAdjacentPartNumbers(rowIndex, gearIndex)
                if(adjacentPartNumbers.size == 2) {
                    result += adjacentPartNumbers[0] * adjacentPartNumbers[1]
                }
            }
        }

        return result
    }

    data class PartNumber(val value: Int, val startIndex: Int, val endIndex: Int)

    private fun String.processRow(): Row {
        val gearIndices = mutableListOf<Int>()
        val indices = mutableListOf<Int>()
        val partNumbers = mutableListOf<PartNumber>()

        val num = StringBuilder()
        var startIndex = -1
        var endIndex: Int

        fun resetCurrentNumber() {
            if(startIndex >= 0) {
                endIndex = startIndex + num.length-1
                partNumbers.add(PartNumber(num.toString().toInt(), startIndex, endIndex))
            }
            num.clear()
            startIndex = -1
            endIndex = -1
        }

        this.withIndex().forEach { (index, char) ->
            if(char == '.') {
                resetCurrentNumber()
            } else if(char.isDigit()) {
                num.append(char)
                if(startIndex == -1) {
                    startIndex = index
                }
                if(index == this.length-1) {
                    resetCurrentNumber()
                }
            } else if (symbols.contains(char)) {
                if(char == '*') {
                    gearIndices.add(index)
                }
                indices.add(index)
                resetCurrentNumber()
            } else {
                println("ERROR: char = $char")
            }
        }

        return Row(this, indices, gearIndices, partNumbers)
    }

    data class Row(val value: String, val symbolIndices: List<Int>, val gearIndices: List<Int>, val partNumbers: List<PartNumber>) {
        fun getValidPartNumbers(neighborIndices: Set<Int>): List<Int> {
            val indices = (symbolIndices+neighborIndices).distinct()
            return partNumbers.filter { partNumber ->
                indices.any { index ->
                    index in partNumber.startIndex-1..partNumber.endIndex+1
                }
            }.map { it.value }
        }

        fun getAdjacentPartNumbers(gearIndex: Int): Collection<Int> {
            val adjacentPartNumbers = mutableListOf<Int>()
            partNumbers.forEach { partNumber ->
                if(gearIndex in partNumber.startIndex-1..partNumber.endIndex+1) {
                    adjacentPartNumbers.add(partNumber.value)
                }
            }
            return adjacentPartNumbers

        }
    }
}