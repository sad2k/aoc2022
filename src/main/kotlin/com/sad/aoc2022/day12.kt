package com.sad.aoc2022

private fun isValidCoordinate(coord: Pair<Int, Int>, input: List<String>): Boolean {
    val (row, col) = coord
    return (row >= 0 && col >= 0 && row < input.size && col < input[0].length)
}

private fun height(coord: Pair<Int, Int>, input: List<String>): Int {
    val ch = input[coord.first][coord.second]
    return when (ch) {
        'S' -> 'a'.code
        'E' -> 'z'.code
        else -> ch.code
    }
}

private fun bfs(startPos: Pair<Int, Int>, input: List<String>): Int? {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val queue = ArrayDeque<Pair<Int, Int>>()
    val distances = mutableMapOf<Pair<Int, Int>, Int>()
    queue.add(startPos)
    distances[startPos] = 0
    while (!queue.isEmpty()) {
        val c = queue.removeFirst()
        if (visited.contains(c)) {
            continue
        }
        val d = distances[c]
        if (input[c.first][c.second] == 'E') {
            return d
        }
        val moves = listOf(
            c.first + 1 to c.second,
            c.first - 1 to c.second,
            c.first to c.second + 1,
            c.first to c.second - 1
        )
            .filter { isValidCoordinate(it, input) }
            .filter { !visited.contains(it) }
            .filter {
                height(it, input) - height(c, input) <= 1
            }
        moves.forEach {
            if (!distances.containsKey(it)) {
                distances[it] = d!! + 1
            }
            queue.add(it)
        }
        visited.add(c)
    }
    return null
}

fun main() {
    val input = loadFromResources("day12.txt").readLines()

    // part 1
    var startPos: Pair<Int, Int>? = null
    for (row in input.indices) {
        for (col in input[row].indices) {
            if (input[row][col] == 'S') {
                startPos = row to col
            }
        }
    }
    check(startPos != null)
    println(bfs(startPos, input))
}