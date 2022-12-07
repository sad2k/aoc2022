package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day6.txt").readFirstLine()

    fun solve(size: Int): Int {
        val windowed = input.windowed(size = size, step = 1)
        for (i in windowed.indices) {
            val w = windowed[i]
            if (w.toSet().size == size) {
                return (i + size)
            }
        }
        throw IllegalArgumentException()
    }

    // part 1
    println(solve(4))

    // part 2
    println(solve(14))
}