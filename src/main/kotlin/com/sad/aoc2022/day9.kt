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

fun follow(head: Coordinates, tail: Coordinates): Coordinates? {
    var newTail: Coordinates? = null
    if ((Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1)) {
        if (head.x == tail.x) {
            // vertical distance
            newTail = tail + Coordinates(0, if (head.y - tail.y > 0) 1 else -1)
        } else if (head.y == tail.y) {
            // horizontal distance
            newTail = tail + Coordinates(if (head.x - tail.x > 0) 1 else -1, 0)
        } else {
            // diagonal distance
            newTail = tail + Coordinates(if (head.x - tail.x > 0) 1 else -1, if (head.y - tail.y > 0) 1 else -1)
        }
    }
    return newTail
}

private fun part1(input: List<Pair<Direction, Int>>) {
    val tailPositions = mutableSetOf<Coordinates>()
    var head = Coordinates(0, 0)
    var tail = Coordinates(0, 0)
    tailPositions.add(tail)
    for ((dir, num) in input) {
        val offset = dir.offset
        for (i in 1..num) {
            head += offset
            val newTail = follow(head, tail)
            if (newTail != null) {
                tail = newTail
                tailPositions.add(tail)
            }
        }
    }
    println(tailPositions.size)
}

private fun part2(input: List<Pair<Direction, Int>>) {
    val tailPositions = mutableSetOf<Coordinates>()
    var head = Coordinates(0, 0)
    var knots = MutableList(9) { Coordinates(0, 0) }
    tailPositions.add(knots[8])
    for ((dir, num) in input) {
        val offset = dir.offset
        for (i in 1..num) {
            head += offset
            var prev: Coordinates = head
            for (k in 0 until knots.size) {
                val newKnot = follow(prev, knots[k])
                if (newKnot != null) {
                    knots[k] = newKnot
                    if (k == 8) {
                        tailPositions.add(newKnot)
                    }
                }
                prev = knots[k]
            }
        }
    }
    println(tailPositions.size)
}

fun main() {
    val input = loadFromResources("day9.txt").readLines().map {
        val (dir, num) = it.split(" ")
        Direction.valueOf(dir) to num.toInt()
    }

    // part 1
    part1(input)

    // part 2
    part2(input)
}

