package com.sad.aoc2022

import java.lang.IllegalStateException

typealias Coord = Pair<Int, Int>

val walls = mutableSetOf<Coord>()
val blizzards = mutableMapOf<Coord, Char>()
val blizzardCache = mutableMapOf<Int, MutableSet<Coord>>()
val blizzardTypeToOffset = mapOf(
    '^' to (-1 to 0),
    'v' to (1 to 0),
    '<' to (0 to -1),
    '>' to (0 to 1)
)
var width = -1
var height = -1

private fun blizzardsAfter(minutes: Int): Set<Coord> {
    var b = blizzardCache[minutes]

    if (b == null) {
        b = mutableSetOf()
        blizzards.entries.forEach { (coord, btype) ->
            val offset = blizzardTypeToOffset[btype]!!
            var (row, col) = coord

            row = Math.floorMod((row - 1 + offset.first * minutes), height) + 1
            col = Math.floorMod((col - 1 + offset.second * minutes), width) + 1
            b.add(row to col)
        }
        blizzardCache[minutes] = b
    }

    return b
}

private fun bfs(start: Coord, end: Coord): Int {
    val visited = mutableSetOf<Pair<Coord, Int>>()
    val queue = ArrayDeque<Pair<Coord, Int>>()
    queue.add(start to 0)
    while (!queue.isEmpty()) {
        val (c, t) = queue.removeFirst()
//        println("c ${c} t ${t}")
        if (visited.contains(c to t)) {
            continue
        }
        visited.add(c to t)
        if (c == end) {
            return t
        }
        val (row, col) = c
        val moves = listOf(
            row to col,
            row + 1 to col,
            row - 1 to col,
            row to col + 1,
            row to col - 1
        )
        val blizzardsNextMin = blizzardsAfter(t + 1)
        moves.filter {
            it.first >= 0 && it.second >= 0 && it.first < height + 2 && it.second < width + 2 &&
                    it !in walls && it !in blizzardsNextMin
        }.forEach {
            queue.add(it to t + 1)
        }
    }
    throw IllegalStateException()
}

fun drawGrove(walls: MutableSet<Pair<Int, Int>>, blizzardsAfter: Set<Pair<Int, Int>>) {
    val res = List(height + 2) {
        StringBuilder(".".repeat(width+2))
    }
    for((row,col) in walls) {
        res[row][col] = '#'
    }
    for((row,col) in blizzardsAfter) {
        res[row][col] = '*'
    }
    for(s in res) {
        println(s)
    }
}

fun main() {
    val input = loadFromResources("day24.txt").readLines()

    width = input[0].length - 2
    height = input.size - 2
    println("width=${width} height=${height}")

    for (i in input.indices) {
        val s = input[i]
        for (j in s.indices) {
            val c = s[j]
            when (c) {
                '#' -> {
                    walls.add(i to j)
                }
                '^', 'v', '<', '>' -> {
                    blizzards[i to j] = c
                }
            }
        }
    }

    println(blizzards)
    println(blizzardsAfter(1))

//    drawGrove(walls, blizzardsAfter(4))

    println(bfs(0 to 1, input.size - 1 to input[0].length - 2))

}


