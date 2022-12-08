package com.sad.aoc2022

import kotlin.math.max

data class TreeScore(var row: Int, var col: Int, var up: Int, var down: Int, var left: Int, var right: Int) {
    val score: Int
        get() = up * down * left * right
}

fun calculateScore(input: List<List<Int>>, row: Int, col: Int, score: TreeScore) {
    val height = input[row][col]
    // up
    var trees = 0
    for (i in row-1 downTo 0) {
        trees++
        if (input[i][col] >= height) {
            break
        }
    }
    score.up = trees
    // down
    trees = 0
    for(i in row+1 until input.size) {
        trees++
        if (input[i][col] >= height) {
            break
        }
    }
    score.down = trees
    // left
    trees = 0
    for(j in col-1 downTo 0) {
        trees++
        if (input[row][j] >= height) {
            break
        }
    }
    score.left = trees
    // right
    trees = 0
    for(j in col+1 until input[row].size) {
        trees++
        if (input[row][j] >= height) {
            break
        }
    }
    score.right = trees
}

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
    println(result.map { it.count { it } }.sum())

    // part 2
    // will naive work?
    // yeah it will..
    val result2 = input.mapIndexed { i, l -> l.mapIndexed { j, _ -> TreeScore(i, j, 0, 0, 0, 0) } }
    for (i in input.indices) {
        for (j in input[i].indices) {
            val score = result2[i][j]
            calculateScore(input, i, j, score)
        }
    }
    println(result2.map { it.maxByOrNull { sc -> sc.score } }.maxByOrNull { sc -> sc!!.score }!!.score)
}


