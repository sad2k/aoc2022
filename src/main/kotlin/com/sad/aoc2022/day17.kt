package com.sad.aoc2022

abstract class ChamberShape {
    abstract val strings: List<String>
    val width
        get() = strings[0].length
    val height
        get() = strings.size

    open fun canMoveDown(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {

        if (row + height >= chamber.size) {
            return false
        }
        val nextRow = chamber[row + height]
        for (i in 0 until width) {
            if (nextRow[col + i] != '.') {
                return false
            }
        }
        return true
    }

    open fun canMoveLeft(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {
        if (col == 0) {
            return false
        }

        for (i in 0 until height) {
            if (chamber[row + i][col - 1] != '.') {
                return false
            }
        }
        return true
    }

    open fun canMoveRight(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {
        if (col + width >= chamber[0].size) {
            return false
        }

        for (i in 0 until height) {
            if (chamber[row + i][col + width] != '.') {
                return false
            }
        }
        return true
    }

    private fun getFullLine(line: String): String {
        return ".." + line + ".".repeat(7 - line.length - 2)
    }

    fun addToChamber(chamber: ArrayDeque<CharArray>) {
        for (i in strings.size - 1 downTo 0) {
            chamber.addFirst(getFullLine(strings[i]).toCharArray())
        }
    }

    open fun reset(chamber: ArrayDeque<CharArray>, row: Int, col: Int) {
        for (stringIdx in strings.indices) {
            val s = strings[stringIdx]
            for (charIdx in s.indices) {
                val ch = s[charIdx]
                if (ch == '#') {
                    chamber[row + stringIdx][col + charIdx] = '.'
                }
            }
        }
    }

    open fun move(chamber: ArrayDeque<CharArray>, row: Int, col: Int, rowOffset: Int, colOffset: Int) {
        reset(chamber, row, col)
        for (stringIdx in strings.indices) {
            val s = strings[stringIdx]
            for (charIdx in s.indices) {
                val ch = s[charIdx]
                if (ch == '#') {
                    chamber[row + rowOffset + stringIdx][col + colOffset + charIdx] = '#'
                }
            }
        }
    }
}

object HorizontalLine : ChamberShape() {
    override val strings = listOf("####")
}

object Plus : ChamberShape() {
    override val strings = listOf(".#.", "###", ".#.")
    override fun canMoveDown(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {
        if (row + height >= chamber.size) {
            return false
        }
        val nextRow = chamber[row + height]
        if (nextRow[col + 1] != '.') {
            return false
        }
        val prevToNextRow = chamber[row + height - 1]
        if (prevToNextRow[col] != '.' || prevToNextRow[col + 2] != '.') {
            return false
        }
        return true
    }

    override fun canMoveLeft(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {
        if (col == 0) {
            return false
        }
        if (chamber[row][col] != '.' || chamber[row + 1][col - 1] != '.' || chamber[row + 2][col] != '.') {
            return false
        }
        return true
    }

    override fun canMoveRight(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {
        if (col + width >= chamber[0].size) {
            return false
        }
        if (chamber[row][col + width - 1] != '.' || chamber[row + 1][col + width] != '.' || chamber[row + 2][col + width - 1] != '.') {
            return false
        }
        return true
    }
}

object InverseL : ChamberShape() {
    override val strings = listOf("..#", "..#", "###")

    override fun canMoveLeft(chamber: ArrayDeque<CharArray>, row: Int, col: Int): Boolean {
        if (col == 0) {
            return false
        }
        if (chamber[row][col + 1] != '.' || chamber[row + 1][col + 1] != '.' || chamber[row + 2][col - 1] != '.') {
            return false
        }
        return true
    }
}

object VerticalLine : ChamberShape() {
    override val strings = listOf("#", "#", "#", "#")
}

object Box : ChamberShape() {
    override val strings = listOf("##", "##")
}

val shapes = listOf(
    HorizontalLine, Plus, InverseL, VerticalLine, Box
)

private fun printChamber(chamber: ArrayDeque<CharArray>) {
    for (ch in chamber) {
        println(ch)
    }
}

private fun solve(input: String, iterations: Long, findCycle: Boolean) {
    val chamber = ArrayDeque<CharArray>()

    var shapeIdx = 0
    var moveIdx = 0
    var trimmedAtBottom = 0
    val cache = mutableMapOf<Triple<String,Int,Int>, Pair<Long,Int>>()
    for (i in 1..iterations) {
        if (findCycle) {
            if (chamber.size >= 100) {
                val cacheKey = Triple(
                    chamber.subList(0, 100).map { String(it) }.joinToString(separator = ""),
                    shapeIdx % shapes.size,
                    moveIdx % input.length
                )
                if (cache.contains(cacheKey)) {
                    println("cycle detected at ${i} (height ${chamber.size + trimmedAtBottom}) (prev = ${cache[cacheKey]})")
                    val (prevIter, prevHeight) = cache[cacheKey]!!
                    val cycledIterations = iterations - prevIter
                    val cycleLength = i - prevIter
                    val fullCyles = cycledIterations / cycleLength
                    val cycleHeight = chamber.size + trimmedAtBottom - prevHeight
                    val fullCycleHeight = fullCyles.toLong() * cycleHeight
                    val remainder = cycledIterations % cycleLength
                    println("Beginning and full cycles height = ${prevHeight + fullCycleHeight}")
                    println("Remainder after last cycle = ${remainder}")
                    return
                } else {
                    cache[cacheKey] = i to chamber.size + trimmedAtBottom
                }
            }
        }

        val shape = shapes[shapeIdx % shapes.size]
        shapeIdx++

        // add three empty rows
        for (j in 1..3) {
            chamber.addFirst(".......".toCharArray())
        }

        shape.addToChamber(chamber)
        var row = 0
        var col = 2

        while (true) {
            val move = input[moveIdx % input.length]
            moveIdx++

            // move L/R
            val moveAllowed =
                if (move == '>') shape.canMoveRight(chamber, row, col) else shape.canMoveLeft(chamber, row, col)
            if (moveAllowed) {
                // draw to L/R
                val offset = if (move == '>') 1 else -1
                shape.move(chamber, row, col, 0, offset)
                col += offset
            }

            // move down
            if (shape.canMoveDown(chamber, row, col)) {
                shape.move(chamber, row, col, 1, 0)
                row += 1
            } else {
                break
            }

            // trim top row
            if (chamber[0].all { it == '.' }) {
                chamber.removeFirst()
                row -= 1
            }
        }

        // trim at the bottom
        if (chamber.size > 100) {
            val toTrim = chamber.size - 100
            for (t in 0 until toTrim) {
                chamber.removeLast()
            }
            trimmedAtBottom += toTrim
        }
    }

    println(chamber.size + trimmedAtBottom)
}

fun main() {
    val input = loadFromResources("day17.txt").readFirstLine()

    // part 1
    //solve(input, 2022, false)

    // part 2
    // sorry dont have time to structure it... a bit of manual labour required in the end
    solve(input, 1000000000000L, true)
    solve(input, 598 + 152, false)
}

