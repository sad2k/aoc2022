package com.sad.aoc2022

import java.lang.IllegalArgumentException
import java.lang.StringBuilder

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
    println("colRanges: ${colRanges}")
    println("rowRanges: ${rowRanges}")
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

private fun part2(
    map: List<String>,
    instructions: List<Any>,
    positionJumpFunction: (pos: Pair<Int, Int>, orientation: Char) -> Triple<Int, Int, Char>
) {
    val colRanges = mutableListOf<Pair<Int, Int>>()
    for (i in map.indices) {
        val s = map[i]
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
    println("colRanges ${colRanges}")
    println("rowRanges ${rowRanges}")
    var row = 0
    var col = colRanges[0].first
    println("starting at ${row},${col}")
    var orientation = 'R'
    for (inst in instructions) {
        println("instruction ${inst}")
        if (inst is Int) {
            for (i in 1..inst) {
                when (orientation) {
                    'R' -> {
                        var candCol = col + 1
                        var candRow = row
                        var candOrientation = orientation
                        if (candCol > colRanges[candRow].second) {
                            val tpl = positionJumpFunction(row to col, orientation)
                            candRow = tpl.first
                            candCol = tpl.second
                            candOrientation = tpl.third
                        }
                        if (map[candRow][candCol] == '#') {
                            break
                        }
                        row = candRow
                        col = candCol
                        orientation = candOrientation
                    }
                    'L' -> {
                        var candCol = col - 1
                        var candRow = row
                        var candOrientation = orientation
                        if (candCol < colRanges[candRow].first) {
                            val tpl = positionJumpFunction(row to col, orientation)
                            candRow = tpl.first
                            candCol = tpl.second
                            candOrientation = tpl.third
                        }
                        if (map[candRow][candCol] == '#') {
                            break
                        }
                        row = candRow
                        col = candCol
                        orientation = candOrientation
                    }
                    'D' -> {
                        var candRow = row + 1
                        var candCol = col
                        var candOrientation = orientation
                        if (candRow > rowRanges[candCol].second) {
                            val tpl = positionJumpFunction(row to col, orientation)
                            candRow = tpl.first
                            candCol = tpl.second
                            candOrientation = tpl.third
                        }
                        if (map[candRow][candCol] == '#') {
                            break
                        }
                        row = candRow
                        col = candCol
                        orientation = candOrientation
                    }
                    'U' -> {
                        var candRow = row - 1
                        var candCol = col
                        var candOrientation = orientation
                        if (candRow < rowRanges[candCol].first) {
                            val tpl = positionJumpFunction(row to col, orientation)
                            candRow = tpl.first
                            candCol = tpl.second
                            candOrientation = tpl.third
                        }
                        if (map[candRow][candCol] == '#') {
                            break
                        }
                        row = candRow
                        col = candCol
                        orientation = candOrientation
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

private fun isGoodSquare(map: List<String>, topLeft: Pair<Int, Int>, bottomRight: Pair<Int, Int>): Boolean {
    var good = false
    for (row in topLeft.first..bottomRight.first) {
        val s = map[row]
        for (col in topLeft.second..bottomRight.second) {
            if (col >= s.length) {
                return false
            }
            if (s[col] == '.' || s[col] == '#') {
                good = true
            }
        }
    }
    return good
}

private fun findSquareBoundaries(map: List<String>, squaresInRow: Int): Map<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    var maxCols = 0
    for (i in map.indices) {
        val s = map[i]
        if (s.length > maxCols) {
            maxCols = s.length
        }
    }
    val squareSide = maxCols / squaresInRow
    val squares = mutableMapOf<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    var squareId = 1
    for (startRow in 0 until map.size step squareSide) {
        for (startCol in 0 until maxCols step squareSide) {
            val topLeft = startRow to startCol
            val bottomRight = startRow + squareSide - 1 to startCol + squareSide - 1
            if (isGoodSquare(map, topLeft, bottomRight)) {
                println("${topLeft} to ${bottomRight}")
                squares[squareId++] = topLeft to bottomRight
            }
        }
    }
    return squares
}

private fun findSquare(row: Int, col: Int, squares: Map<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
    for ((id, coords) in squares.entries) {
        if (row >= coords.first.first && row <= coords.second.first && col >= coords.first.second && col <= coords.second.second) {
            return id
        }
    }
    throw IllegalArgumentException("${row},${col}")
}

private fun examplePositionJumpFunction(
    coord: Pair<Int, Int>,
    orientation: Char,
    squares: Map<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>
): Triple<Int, Int, Char> {
    val (row, col) = coord
    val square = findSquare(row, col, squares)
    var newRow = -1
    var newCol = -1
    var newOrientation = '?'
    when (square) {
        1 -> {
            when (orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[3]!!.first.first
                    newCol = squares[3]!!.first.second + rowOffset
                    newOrientation = 'D'
                }
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[6]!!.second.first - rowOffset
                    newCol = squares[6]!!.second.second
                    newOrientation = 'L'
                }
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[2]!!.first.first
                    newCol = squares[2]!!.second.second - colOffset
                    newOrientation = 'D'
                }
            }
        }
        2 -> {
            when(orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[6]!!.second.first
                    newCol = squares[6]!!.second.second - rowOffset
                    newOrientation = 'U'
                }
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[1]!!.first.first
                    newCol = squares[1]!!.second.second - colOffset
                    newOrientation = 'D'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[5]!!.second.first
                    newCol = squares[5]!!.second.second - colOffset
                    newOrientation = 'U'
                }
            }
        }
        3 -> {
            when(orientation) {
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[1]!!.first.first + colOffset
                    newCol = squares[1]!!.first.second
                    newOrientation = 'R'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[5]!!.second.first - colOffset
                    newCol = squares[5]!!.first.second
                    newOrientation = 'R'
                }
            }
        }
        4 -> {
            when(orientation) {
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[6]!!.first.first
                    newCol = squares[6]!!.second.second - rowOffset
                    newOrientation = 'D'
                }
            }
        }
        5 -> {
            when(orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[3]!!.second.first
                    newCol = squares[3]!!.second.second - rowOffset
                    newOrientation = 'U'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[2]!!.second.first
                    newCol = squares[2]!!.second.second - colOffset
                    newOrientation = 'U'
                }
            }
        }
        6 -> {
            when(orientation) {
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[4]!!.second.first - colOffset
                    newCol = squares[4]!!.second.second
                    newOrientation = 'L'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[2]!!.second.first - colOffset
                    newCol = squares[2]!!.first.second
                    newOrientation = 'R'
                }
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[1]!!.second.first - rowOffset
                    newCol = squares[1]!!.second.second
                    newOrientation = 'L'
                }
            }
        }
    }
    if (newRow < 0 || newCol < 0 || newOrientation == '?') {
        throw IllegalArgumentException("coord=${coord} orientation=${orientation}")
    }
    return Triple(newRow, newCol, newOrientation)
}

private fun testPositionJumpFunction(
    coord: Pair<Int, Int>,
    orientation: Char,
    squares: Map<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>
): Triple<Int, Int, Char> {
    val (row, col) = coord
    val square = findSquare(row, col, squares)
    var newRow = -1
    var newCol = -1
    var newOrientation = '?'
    println("jump for ${coord} orient ${orientation}")
    when (square) {
        1 -> {
            when(orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[4]!!.second.first - rowOffset
                    newCol = squares[4]!!.first.second
                    newOrientation = 'R'
                }
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[6]!!.first.first + colOffset
                    newCol = squares[6]!!.first.second
                    newOrientation = 'R'
                }
            }
        }
        2 -> {
            when(orientation) {
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newCol = squares[6]!!.first.second + colOffset
                    newRow = squares[6]!!.second.first
                    newOrientation = 'U'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[3]!!.first.first + colOffset
                    newCol = squares[3]!!.second.second
                    newOrientation = 'L'
                }
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[5]!!.second.first - rowOffset
                    newCol = squares[5]!!.second.second
                    newOrientation = 'L'
                }
            }
        }
        3 -> {
            when(orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[4]!!.first.first
                    newCol = squares[4]!!.first.second + rowOffset
                    newOrientation = 'D'
                }
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[2]!!.second.first
                    newCol = squares[2]!!.first.second + rowOffset
                    newOrientation = 'U'
                }
            }
        }
        4 -> {
            when(orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[1]!!.second.first - rowOffset
                    newCol = squares[1]!!.first.second
                    newOrientation = 'R'
                }
                'U' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[3]!!.first.first + colOffset
                    newCol = squares[3]!!.first.second
                    newOrientation = 'R'
                }
            }

        }
        5 -> {
            when(orientation) {
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[2]!!.second.first - rowOffset
                    newCol = squares[2]!!.second.second
                    newOrientation = 'L'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[6]!!.first.first + colOffset
                    newCol = squares[6]!!.second.second
                    newOrientation = 'L'
                }
            }
        }
        6 -> {
            when(orientation) {
                'L' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[1]!!.first.first
                    newCol = squares[1]!!.first.second + rowOffset
                    newOrientation = 'D'
                }
                'D' -> {
                    val colOffset = col - squares[square]!!.first.second
                    newRow = squares[2]!!.first.first
                    newCol = squares[2]!!.first.second + colOffset
                    newOrientation = 'D'
                }
                'R' -> {
                    val rowOffset = row - squares[square]!!.first.first
                    newRow = squares[5]!!.second.first
                    newCol = squares[5]!!.first.second + rowOffset
                    newOrientation = 'U'
                }
            }
        }
    }
    if (newRow < 0 || newCol < 0 || newOrientation == '?') {
        throw IllegalArgumentException("coord=${coord} orientation=${orientation}")
    }
    val res = Triple(newRow, newCol, newOrientation)
    println("jump from ${coord} (orient ${orientation} sq ${square}) to ${res}")
    return res

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
//    part1(map, instructions)

    // part 1
    val squares = findSquareBoundaries(map, 3)
    println(squares)
    part2(map, instructions) { pos, orientation ->
        testPositionJumpFunction(pos, orientation, squares)
    }
}

