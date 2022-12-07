package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day6.txt").readFirstLine()

    // part 1
    val windowed = input.windowed(size = 4, step = 1)
    for (i in windowed.indices) {
        val w = windowed[i]
        if (w.toSet().size == 4) {
            println(i + 4)
            break
        }
    }
}