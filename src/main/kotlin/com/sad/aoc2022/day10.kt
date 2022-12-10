package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day10.txt").readLines()

    // part 1
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