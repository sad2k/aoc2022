package com.sad.aoc2022

import kotlin.math.abs

data class Coordinates(val x: Int, val y: Int) {
    operator fun plus(other: Coordinates): Coordinates =
        Coordinates(x + other.x, y + other.y)
}

enum class Direction(val offset: Coordinates) {
    U(Coordinates(0, 1)), D(Coordinates(0, -1)),
    L(Coordinates(-1, 0)), R(Coordinates(1, 0))
}

fun main() {
    val input = loadFromResources("day9.txt").readLines().map {
        val (dir, num) = it.split(" ")
        Direction.valueOf(dir) to num.toInt()
    }

    // part 1
    val tailPositions = mutableSetOf<Coordinates>()
    var head = Coordinates(0, 0)
    var tail = Coordinates(0, 0)
    tailPositions.add(tail)
    for ((dir, num) in input) {
        val offset = dir.offset
        for (i in 1..num) {
            head += offset
            if ((Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1)) {
                if (head.x == tail.x) {
                    // vertical distance
                    tail += Coordinates(0, if (head.y - tail.y > 0) 1 else -1)
                } else if (head.y == tail.y) {
                    // horizontal distance
                    tail += Coordinates(if (head.x - tail.x > 0) 1 else -1, 0)
                } else {
                    // diagonal distance
                    tail += Coordinates(if (head.x - tail.x > 0) 1 else -1, if (head.y - tail.y > 0) 1 else -1)
                }
                tailPositions.add(tail)
            }
        }
    }
    println(tailPositions.size)
}