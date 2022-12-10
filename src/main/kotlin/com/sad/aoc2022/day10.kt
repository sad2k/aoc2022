package com.sad.aoc2022

private fun part1(input: List<String>) {
    var cycle = 1
    var x = 1
    var result = 0L
    val cycles = listOf(20, 60, 100, 140, 180, 220)
    for (line in input) {
        val spl = line.split(" ")
        var cyclesToWait = 0
        var add = 0
        when (spl[0]) {
            "noop" -> {
                cyclesToWait = 1
                add = 0
            }
            "addx" -> {
                cyclesToWait = 2
                add = spl[1].toInt()
            }
            else -> throw IllegalArgumentException()
        }
        for (i in 1..cyclesToWait) {
            if (cycle in cycles) {
                result += (cycle * x)
            }
            cycle++
        }
        x += add
    }
    println(result)
}

private fun part2(input: List<String>) {
    var cycle = 1
    var x = 1
    var sb = StringBuilder()
    var xpos = 0
    for (line in input) {
        val spl = line.split(" ")
        var cyclesToWait = 0
        var add = 0
        when (spl[0]) {
            "noop" -> {
                cyclesToWait = 1
                add = 0
            }
            "addx" -> {
                cyclesToWait = 2
                add = spl[1].toInt()
            }
            else -> throw IllegalArgumentException()
        }
        for (i in 1..cyclesToWait) {
            val sprite = listOf(x - 1, x, x + 1)
            if (xpos in sprite) {
                sb.append("#")
            } else {
                sb.append(".")
            }
            cycle++
            xpos++
            if (xpos == 40) {
                xpos = 0
                sb.append("\n")
            }
        }
        x += add
    }
    println(sb)
}

fun main() {
    val input = loadFromResources("day10.txt").readLines()

    // part 1
    part1(input)

    // part 2
    part2(input)
}

