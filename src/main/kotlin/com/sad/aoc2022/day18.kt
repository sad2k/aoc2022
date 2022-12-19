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

private fun countSidesExposedToOutside(coord: Triple<Int, Int, Int>, cubes: Set<Triple<Int, Int, Int>>, outsideCoords: Set<Triple<Int,Int,Int>>): Int {
    return neighbours(coord).filter {
        !cubes.contains(it) && outsideCoords.contains(it)
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

    // part 2
    val maxx = input.maxOf { it.first } + 1
    val maxy = input.maxOf { it.second } + 1
    val maxz = input.maxOf { it.third } + 1
    val queue = ArrayDeque<Triple<Int, Int, Int>>()
    val visited = mutableSetOf<Triple<Int, Int, Int>>()
    queue.add(Triple(maxx, maxy, maxz))
    while (!queue.isEmpty()) {
        val c = queue.removeFirst()
        if (visited.contains(c)) {
            continue
        }
        val neighbours = neighbours(c).filter {
            it.first <= maxx && it.second <= maxy && it.third <= maxz &&
                    it.first >= -1 && it.second >= -1 && it.third >= -1 &&
                    !input.contains(it)
        }
        neighbours.forEach {
            queue.add(it)
        }
        visited.add(c)
    }
//    println(visited)
    println(input.map {
        countSidesExposedToOutside(it, input, visited)
    }.sum())

}