package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day5.txt").readLines().splitWhen { it.isBlank() }

    // part 1
    val stackLines = input[0].take(input[0].size - 1)
    val stackDef = input[0][input[0].size - 1]
    val stackIdx = mutableMapOf<Int, Int>()
    stackDef.forEachIndexed { idx, v ->
        if (v.isDigit()) {
            stackIdx[idx] = v.digitToInt()
        }
    }
    val stacks = MutableList(stackIdx.size) { mutableListOf<Char>() }
    stackLines.forEach {
        it.forEachIndexed { idx, v ->
            if (v.isLetter()) {
                stacks[stackIdx[idx]!! - 1] += v
            }
        }
    }
    val instructions = input[1].map {
        val spl = it.split(" ")
        Triple(spl[1].toInt(), spl[3].toInt(), spl[5].toInt())
    }
    instructions.forEach { (num, from, to) ->
        for (i in 0 until num) {
            stacks[to - 1].add(0, stacks[from - 1].removeFirst())
        }
    }
    println(stacks.map { it[0] }.joinToString(separator = ""))
}