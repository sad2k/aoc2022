package com.sad.aoc2022

import java.lang.IllegalArgumentException
import java.lang.StringBuilder

//private fun isValidPosition(row: Int, col: Int, map: List<String>) {
//
//}

private fun part1(map: List<String>, instructions: List<Any>) {
    val colRanges = mutableListOf<Pair<Int, Int>>()
    for (i in map.indices) {
        val s = map[i]
        println(s)
        val firstCol = s.indexOfAny(charArrayOf('.', '#'))
        val lastCol = s.lastIndexOfAny(charArrayOf('.', '#'))
        colRanges.add(firstCol to lastCol)
    }
    val maxCol = colRanges.map { it.second }.maxOf { it }
    val rowRanges = mutableListOf<Pair<Int, Int>>()
    for (i in 0..maxCol) {
        var minRow = -1
        var maxRow = -1
        for (j in map.indices) {
            val s = map[j]
            if (i < s.length && (s[i] == '.' || s[i] == '#')) {
                if (minRow == -1) {
                    minRow = j
                }
                maxRow = j
            }
        }
        rowRanges.add(minRow to maxRow)
    }

    var row = 0
    var col = colRanges[0].first
    println("starting at ${row},${col}")
    var orientation = 'R'
    for (inst in instructions) {
        if (inst is Int) {
            when (orientation) {
                'R' -> {
                    for (i in 1..inst) {
                        var candCol = col + 1
                        if (candCol > colRanges[row].second) {
                            candCol = colRanges[row].first
                        }
                        if (map[row][candCol] == '#') {
                            break
                        }
                        col = candCol
                    }
                }
                'L' -> {
                    for (i in 1..inst) {
                        var candCol = col - 1
                        if (candCol < colRanges[row].first) {
                            candCol = colRanges[row].second
                        }
                        if (map[row][candCol] == '#') {
                            break
                        }
                        col = candCol

                    }
                }
                'D' -> {
                    for (i in 1..inst) {
                        var candRow = row + 1
                        if (candRow > rowRanges[col].second) {
                            candRow = rowRanges[col].first
                        }
                        if (map[candRow][col] == '#') {
                            break
                        }
                        row = candRow
                    }
                }
                'U' -> {
                    for (i in 1..inst) {
                        var candRow = row - 1
                        if (candRow < rowRanges[col].first) {
                            candRow = rowRanges[col].second
                        }
                        if (map[candRow][col] == '#') {
                            break
                        }
                        row = candRow
                    }
                }
            }
        } else if (inst is Char) {
            when (inst) {
                'R' -> {
                    when (orientation) {
                        'R' -> orientation = 'D'
                        'D' -> orientation = 'L'
                        'L' -> orientation = 'U'
                        'U' -> orientation = 'R'
                    }
                }
                'L' -> {
                    when (orientation) {
                        'R' -> orientation = 'U'
                        'U' -> orientation = 'L'
                        'L' -> orientation = 'D'
                        'D' -> orientation = 'R'
                    }
                }
            }
        } else {
            throw IllegalArgumentException(inst.toString())
        }
    }
    println("${row + 1}, ${col + 1}")
    val orientationScore = when (orientation) {
        'R' -> 0
        'D' -> 1
        'L' -> 2
        'U' -> 3
        else -> throw IllegalArgumentException(orientation.toString())
    }
    println("${1000 * (row + 1) + 4 * (col + 1) + orientationScore}")
}

fun main() {
    val input = loadFromResources("day22.txt").readLines().splitWhen { it.isBlank() }
    val map = input[0]
    val instructionsStr = input[1][0]

    val instructions = mutableListOf<Any>()
    val sb = StringBuilder()
    for (ch in instructionsStr) {
        if (ch.isLetter()) {
            if (!sb.isEmpty()) {
                instructions.add(sb.toString().toInt())
            }
            instructions.add(ch)
            sb.setLength(0)
        } else {
            sb.append(ch)
        }
    }
    if (!sb.isEmpty()) {
        instructions.add(sb.toString().toInt())
    }
    println(instructions)


    // part 1
    part1(map, instructions)

}

