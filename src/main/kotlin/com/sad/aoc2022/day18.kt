package com.sad.aoc2022

private fun neighbours(coord: Triple<Int, Int, Int>): List<Triple<Int, Int, Int>> {
    val (x, y, z) = coord
    return listOf(
        Triple(x + 1, y, z),
        Triple(x - 1, y, z),
        Triple(x, y + 1, z),
        Triple(x, y - 1, z),
        Triple(x, y, z + 1),
        Triple(x, y, z - 1)
    )
}


private fun countExposedSides(coord: Triple<Int, Int, Int>, cubes: Set<Triple<Int, Int, Int>>): Int {
    return neighbours(coord).filter {
        !cubes.contains(it)
    }.count()
}

fun main() {
    val input = loadFromResources("day18.txt").readLines().map {
        val l = it.split(",").map { s ->
            s.toInt()
        }
        Triple(l[0], l[1], l[2])
    }.toSet()

    // part 1
    println(input.map {
        countExposedSides(it, input)
    }.sum())
}