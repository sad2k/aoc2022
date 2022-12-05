package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day5.txt").readLines().splitWhen { it.isBlank() }
    val stackLines = input[0].take(input[0].size - 1)
    val stackDef = input[0][input[0].size - 1]
    val stackIdx = mutableMapOf<Int, Int>()
    stackDef.forEachIndexed { idx, v ->
        if (v.isDigit()) {
            stackIdx[idx] = v.digitToInt()
        }
    }
    val stacks = List(stackIdx.size) { mutableListOf<Char>() }
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

    // part 1
    val target = stacks.map { it.toMutableList() }
    instructions.forEach { (num, from, to) ->
        for (i in 0 until num) {
            target[to - 1].add(0, target[from - 1].removeFirst())
        }
    }
    println(target.map { it[0] }.joinToString(separator = ""))

    // part 2
    val target2 = stacks.map { it.toMutableList() }
    instructions.forEach { (num, from, to) ->
        val tempList = mutableListOf<Char>()
        for (i in 0 until num) {
            tempList.add(target2[from - 1].removeFirst())
        }
        for (i in tempList.size - 1 downTo 0) {
            target2[to - 1].add(0, tempList[i])
        }
    }
    println(target2.map { it[0] }.joinToString(separator = ""))
}