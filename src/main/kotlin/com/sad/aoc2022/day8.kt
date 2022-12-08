package com.sad.aoc2022

import kotlin.math.max

fun main() {
    val input = loadFromResources("day8.txt").readLines().map {
        it.map { ch ->
            ch.digitToInt()
        }
    }

    // part 1
    val result = input.map { it.map { false }.toMutableList() }
    // walk rows
    for (i in input.indices) {
        // walk row left to right
        var highest = -1
        for (j in input[i].indices) {
            if (input[i][j] > highest) {
                result[i][j] = true
            }
            highest = max(highest, input[i][j])
        }
        // walk row right to left
        highest = -1
        for (j in input[i].size - 1 downTo 0) {
            if (input[i][j] > highest) {
                result[i][j] = true
            }
            highest = max(highest, input[i][j])
        }
    }
    // walk columns
    for (j in input[0].indices) {
        // walk top to bottom
        var highest = -1
        for (i in input.indices) {
            if (input[i][j] > highest) {
                result[i][j] = true
            }
            highest = max(highest, input[i][j])
        }
        // walk bottom to top
        highest = -1
        for (i in input.size - 1 downTo 0) {
            if (input[i][j] > highest) {
                result[i][j] = true
            }
            highest = max(highest, input[i][j])
        }
    }
    println(result.map { it.count { it }}.sum())

}