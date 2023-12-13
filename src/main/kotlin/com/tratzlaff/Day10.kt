package com.tratzlaff

// https://adventofcode.com/2023/day/10
fun main() {
    val day = Day10()
    day.solve(readLines("day10.txt"))
}

class Day10 {

    fun solve(lines: List<String>) {
        val loop = getConnectedPathList(lines)
        println("Part 1: ${loop.size / 2}")

        val grid = lines.expand().toMutableList()
        val expandedLoop = getConnectedPathList2(grid)

        fun fill(loc: Pair<Int, Int>) {
            fun Pair<Int, Int>.isValid(): Boolean {
                if(expandedLoop.any { it == this }) return false
                if(this.first < 0 || this.first >= grid.size) return false
                if(this.second < 0 || this.second >= grid[0].length) return false
                if(grid[this.first][this.second] == 'X') return false
                return true
            }

            val stack = ArrayDeque<Pair<Int, Int>>()
            stack.add(loc)

            while (stack.isNotEmpty()) {
                val current = stack.removeLast()

                if (!current.isValid()) continue

                grid[current.first] = grid[current.first].substring(0, current.second) + "X" + grid[current.first].substring(current.second + 1)

                val north = Pair(current.first - 1, current.second)
                val south = Pair(current.first + 1, current.second)
                val east = Pair(current.first, current.second + 1)
                val west = Pair(current.first, current.second - 1)

                if (north.isValid()) stack.add(north)
                if (south.isValid()) stack.add(south)
                if (east.isValid()) stack.add(east)
                if (west.isValid()) stack.add(west)
            }
        }

        fill(0 to 0)

        val finalGrid = grid.unexpanded()

        println("Part 2: ${finalGrid.count(loop)}")
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

    private fun List<String>.count(loop: List<Pair<Int, Int>>): Int {
        println(loop)
        var enclosed = 0
        this.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                if(char != 'X' && loop.none { it.first == row && it.second == col }) {
                    println("row: $row, col: $col, char: $char")
                    enclosed++
                }

            }
        }

        return enclosed
    }

    private fun List<String>.expand(): List<String> {
        val expanded = mutableListOf<String>()
        val emptyLine = ".".repeat(this[0].length * 2 + 1)
        this.forEach { line ->
            expanded.add(emptyLine)
            expanded.add(line.map {".$it" }.joinToString("") + ".")
        }
        expanded.add(emptyLine)
        return expanded
    }

    private fun List<String>.unexpanded(): List<String> {
        return this.filterIndexed { index, _ -> index % 2 != 0 }
            .map { it.filterIndexed { index, _ -> index % 2 != 0 } }
    }

    private fun getConnectedPathList2(lines: List<String>): List<Pair<Int, Int>> {
        val loop = mutableListOf(getStart(lines))
        val maxRowIndex = lines.size - 1
        val maxColIndex = lines[0].length - 1

        fun Pair<Int, Int>.isValid(): Boolean {
            if ((loop.size > 3 && this == loop[loop.size-3]) || this == loop.first()) return false
            if (loop.size - 2 >= 1 && this == loop[loop.size - 2]) return false
            return this.first in 0..maxRowIndex && this.second >= 0 && this.second <= maxColIndex
        }

        while(true) {
            val previousRowIndex = loop.last().first
            val previousColIndex = loop.last().second
            //println("${lines[previousRowIndex][previousColIndex]}, previousRowIndex: $previousRowIndex, previousColIndex: $previousColIndex")
            when (val previousPipe = lines[previousRowIndex][previousColIndex]) {
                '7' -> {
                    val south = Pair(previousRowIndex + 2, previousColIndex)
                    val west = Pair(previousRowIndex, previousColIndex - 2)

                    if(loop.size > 10) {
                        if (west == loop.first()) {
                            loop.add(west.copy(second = west.second + 1))
                            break
                        }
                        if (south == loop.first() && "|LJ".any { it == lines[south.first][south.second] }) {
                            loop.add(south.copy(first = south.first - 1))
                            break
                        }
                    }

                    if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south.copy(first = south.first-1))
                        loop.add(south)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west.copy(second = west.second+1))
                        loop.add(west)
                    } else break
                }
                'F' -> {
                    val south = Pair(previousRowIndex + 2, previousColIndex)
                    val east = Pair(previousRowIndex, previousColIndex + 2)

                    if(loop.size > 10) {
                        if (east == loop.first()) {
                            loop.add(east.copy(second = east.second - 1))
                            break
                        }
                        if (south == loop.first()) {
                            loop.add(south.copy(first = south.first - 1))
                            break
                        }
                    }

                    if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south.copy(first = south.first-1))
                        loop.add(south)
                    } else if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east.copy(second = east.second-1))
                        loop.add(east)
                    } else break
                }
                'L' -> {
                    val north = Pair(previousRowIndex - 2, previousColIndex)
                    val east = Pair(previousRowIndex, previousColIndex + 2)

                    if(loop.size > 10) {
                        if (east == loop.first()) {
                            loop.add(east.copy(second = east.second - 1))
                            break
                        }
                        if (north == loop.first()) {
                            loop.add(north.copy(first = north.first + 1))
                            break
                        }
                    }

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north.copy(first = north.first+1))
                        loop.add(north)
                    } else if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east.copy(second = east.second-1))
                        loop.add(east)
                    } else break
                }
                'J' -> {
                    val north = Pair(previousRowIndex - 2, previousColIndex)
                    val west = Pair(previousRowIndex, previousColIndex - 2)

                    if(loop.size > 10) {
                        if (west == loop.first()) {
                            loop.add(west.copy(second = west.second + 1))
                            break
                        }
                        if (north == loop.first()) {
                            loop.add(north.copy(first = north.first + 1))
                            break
                        }
                    }

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north.copy(first = north.first+1))
                        loop.add(north)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west.copy(second = west.second+1))
                        loop.add(west)
                    } else break
                }
                '|' -> {
                    val north = Pair(previousRowIndex - 2, previousColIndex)
                    val south = Pair(previousRowIndex + 2, previousColIndex)

                    if(loop.size > 10)  {
                        if (north == loop.first()) {
                            loop.add(north.copy(first = north.first + 1))
                            break
                        }
                        if (south == loop.first()) {
                            loop.add(south.copy(first = south.first - 1))
                            break
                        }
                    }

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north.copy(first = north.first+1))
                        loop.add(north)
                    } else if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south.copy(first = south.first-1))
                        loop.add(south)
                    } else break
                }
                '-' -> {
                    val east = Pair(previousRowIndex, previousColIndex + 2)
                    val west = Pair(previousRowIndex, previousColIndex - 2)

                    if(loop.size > 10) {
                        if (east == loop.first()) {
                            loop.add(east.copy(second = east.second - 1))
                            break
                        }
                        if (west == loop.first()) {
                            loop.add(west.copy(second = west.second + 1))
                            break
                        }
                    }

                    if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east.copy(second = east.second-1))
                        loop.add(east)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west.copy(second = west.second+1))
                        loop.add(west)
                    } else break
                }
                'S' -> {
                    val north = Pair(previousRowIndex - 2, previousColIndex)
                    val south = Pair(previousRowIndex + 2, previousColIndex)
                    val east = Pair(previousRowIndex, previousColIndex + 2)
                    val west = Pair(previousRowIndex, previousColIndex - 2)

                    if (north.isValid() && "|7F".any { it == lines[north.first][north.second] }) {
                        loop.add(north.copy(first = north.first+1))
                        loop.add(north)
                    } else if (south.isValid() && "|LJ".any { it == lines[south.first][south.second] }) {
                        loop.add(south.copy(first = south.first-1))
                        loop.add(south)
                    } else if (east.isValid() && "-7J".any { it == lines[east.first][east.second] }) {
                        loop.add(east.copy(second = east.second-1))
                        loop.add(east)
                        loop.add(east)
                    } else if (west.isValid() && "-FL".any { it == lines[west.first][west.second] }) {
                        loop.add(west.copy(second = west.second+1))
                        loop.add(west)
                    } else break
                }
                else -> throw Exception("Invalid pipe: $previousPipe")
            }
        }

        return loop
    }
}