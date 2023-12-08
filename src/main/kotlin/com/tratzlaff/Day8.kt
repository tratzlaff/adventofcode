package com.tratzlaff

fun main() {
    val day = Day8()
    println("Part 1: "+day.solvePart1(readLines("day8.txt")))
    println("Part 2: "+day.solvePart2(readLines("day8.txt")))
}

// https://adventofcode.com/2023/day/8
class Day8{
    fun solvePart1(lines: List<String>): Long {
        val instructions = lines.first().trim()
        val network = lines.subList(2, lines.size).associate { it.toNode() }
        return camelRide("AAA", instructions, network)
    }

    private fun camelRide(nodeName: String, instructions: String, network: Map<String, List<String>>): Long {
        val queue = ArrayDeque<Char>()
        instructions.forEach { queue.add(it) }

        var steps = 0L
        var current = nodeName
        while(queue.isNotEmpty()) {
            steps++
            val instruction = queue.removeFirst()
            val node = network[current]!!
            val next = if(instruction == 'L') node.first() else node.last()
            if(next == "ZZZ") return steps
            current = next
        }
        return camelRide(current, instructions, network) + steps
    }

    private fun String.toNode(): Pair<String, List<String>> {
        val (key, value) = this.split("=", limit = 2).map { it.trim() }
        val list = value.removeSurrounding("(", ")").split(",").map { it.trim() }
        return key to list
    }

    fun solvePart2(lines: List<String>): Long {
        val instructions = lines.first().trim()
        val network = lines.subList(2, lines.size).associate { it.toNode() }
        val startingNodes = network.keys.filter { it.endsWith("A") }.toSet()
        return camelRide2(startingNodes, instructions, network)
    }

    /**
     * @return The Least Common Multiple (LCM) of the path lengths from starting nodes to nodes ending with 'Z'.
     */
    private fun camelRide2(startingNodes: Set<String>, instructions: String, network: Map<String, List<String>>): Long {
        val pathLengths = mutableListOf<Long>()

        for (startNode in startingNodes) {
            var currentNode = startNode
            var steps = 0L
            val visited = mutableSetOf<String>()

            while (!currentNode.endsWith("Z")) {
                // If the current node has been visited before, clear the visited set.
                // This allows for paths that revisit nodes but eventually lead to 'Z'.
                if (visited.contains(currentNode)) {
                    visited.clear()
                }
                visited.add(currentNode)

                // Get the next instruction and move to the next node accordingly
                val instruction = instructions[(steps % instructions.length).toInt()]
                currentNode = when(instruction) {
                    'L' -> network[currentNode]!!.first()
                    'R' -> network[currentNode]!!.last()
                    else -> throw IllegalArgumentException("Invalid instruction: $instruction")
                }

                steps++
            }

            // Record the path length as it successfully ends with 'Z'
            pathLengths.add(steps)
        }

        // Calculate LCM of all valid path lengths
        return pathLengths.fold(1L, ::lcm)
    }

    /**
     * @return the Least Common Multiple (LCM) of two numbers
     */
    fun lcm(a: Long, b: Long): Long {
        fun gcd(x: Long, y: Long): Long {
            return if (y == 0L) x else gcd(y, x % y)
        }
        return (a / gcd(a, b)) * b
    }
}