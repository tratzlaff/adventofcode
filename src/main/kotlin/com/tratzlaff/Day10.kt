package com.tratzlaff

// https://adventofcode.com/2023/day/10
fun main() {
    val day = Day10()
    println("Part 1: "+day.solvePart1(readLines("day10.txt")))
}

class Day10 {
    fun solvePart1(lines: List<String>): Int {
        val loop = getConnectedPathList(lines)
        return loop.size / 2
    }

    private fun getStart(list: List<String>): Pair<Int, Int> {
        for (row in list.indices) {
            for (col in list[row].indices) {
                if (list[row][col] == 'S') {
                    return Pair(row, col)
                }
            }
        }
        throw Exception("No starting point found")
    }

    private fun getConnectedPathList(lines: List<String>): List<Pair<Int, Int>> {
        val loop = mutableListOf(getStart(lines))

        while(true) {
            val maxRowIndex = lines.size - 1
            val maxColIndex = lines[0].length - 1

            fun Pair<Int, Int>.isValid(): Boolean {
                if (this == loop.last() || this == loop.first()) return false
                if (loop.size - 2 >= 1 && this == loop[loop.size - 2]) return false
                return this.first in 0..maxRowIndex && this.second >= 0 && this.second <= maxColIndex
            }

            val previousRowIndex = loop.last().first
            val previousColIndex = loop.last().second
            when (val previousPipe = lines[previousRowIndex][previousColIndex]) {
                '7' -> {
                    val south = Pair(previousRowIndex + 1, previousColIndex)
                    val west = Pair(previousRowIndex, previousColIndex - 1)

                    if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west)
                    } else break
                }

                'F' -> {
                    val south = Pair(previousRowIndex + 1, previousColIndex)
                    val east = Pair(previousRowIndex, previousColIndex + 1)

                    if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south)
                    } else if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east)
                    } else break
                }

                'L' -> {
                    val north = Pair(previousRowIndex - 1, previousColIndex)
                    val east = Pair(previousRowIndex, previousColIndex + 1)

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north)
                    } else if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east)
                    } else break
                }

                'J' -> {
                    val north = Pair(previousRowIndex - 1, previousColIndex)
                    val west = Pair(previousRowIndex, previousColIndex - 1)

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west)
                    } else break
                }

                '|' -> {
                    val north = Pair(previousRowIndex - 1, previousColIndex)
                    val south = Pair(previousRowIndex + 1, previousColIndex)

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north)
                    } else if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south)
                    } else break
                }

                '-' -> {
                    val east = Pair(previousRowIndex, previousColIndex + 1)
                    val west = Pair(previousRowIndex, previousColIndex - 1)

                    if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west)
                    } else break
                }

                'S' -> {
                    val north = Pair(previousRowIndex - 1, previousColIndex)
                    val south = Pair(previousRowIndex + 1, previousColIndex)
                    val east = Pair(previousRowIndex, previousColIndex + 1)
                    val west = Pair(previousRowIndex, previousColIndex - 1)

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north)
                    } else if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south)
                    } else if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west)
                    } else break
                }

                else -> throw Exception("Invalid pipe: $previousPipe")
            }
        }

        return loop
    }

}