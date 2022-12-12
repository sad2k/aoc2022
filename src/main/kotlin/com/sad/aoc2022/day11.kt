package com.sad.aoc2022

data class Monkey(
    val items: MutableList<Long>, val op: Char, val leftOp: Long, val rightOp: Long, val divisibleBy: Long,
    val trueMonkey: Int, val falseMonkey: Int, var inspectionCount: Long
)

private fun toMonkey(lines: List<String>): Monkey {
    val startingItems = mutableListOf<Long>()
    var op = '?'
    var leftOp = -1L
    var rightOp = -1L
    var divisibleBy = -1L
    var trueMonkey = -1
    var falseMonkey = -1
    for ((i, line) in lines.withIndex()) {
        if (i > 0) {
            val spl = line.split(":")
            if (i == 1) {
                // starting items
                startingItems.addAll(spl[1].split(",").map(String::trim).map(String::toLong))
            } else if (i == 2) {
                // operation
                val spl2 = spl[1].split("=")[1].trim().split(" ")
                op = spl2[1].trim()[0]
                leftOp = if (spl2[0] == "old") -1 else spl2[0].toLong()
                rightOp = if (spl2[2] == "old") -1 else spl2[2].toLong()
            } else if (i == 3) {
                // divisible by
                val spl2 = spl[1].split(" ")
                divisibleBy = spl2[spl2.size - 1].toLong()
            } else if (i == 4) {
                // if true
                val spl2 = spl[1].split(" ")
                trueMonkey = spl2[spl2.size - 1].toInt()
            } else if (i == 5) {
                // if false
                val spl2 = spl[1].split(" ")
                falseMonkey = spl2[spl2.size - 1].toInt()
            }
        }
    }
    return Monkey(startingItems, op, leftOp, rightOp, divisibleBy, trueMonkey, falseMonkey, 0L)
}

private fun solve(monkeys: List<Monkey>, rounds: Int, worryReducingFactor: Long) {
    val divisibleByProduct = monkeys.map(Monkey::divisibleBy).reduce { acc, i -> acc * i }
    for(round in 1..rounds) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.inspectionCount++
                // new level
                val left = if (monkey.leftOp == -1L) item else monkey.leftOp
                val right = if (monkey.rightOp == -1L) item else monkey.rightOp
                var newLevel = if (monkey.op == '*') left * right else left + right
                newLevel /= worryReducingFactor
                newLevel %= divisibleByProduct
                if (newLevel % monkey.divisibleBy == 0L) {
                    monkeys[monkey.trueMonkey].items.add(newLevel)
                } else {
                    monkeys[monkey.falseMonkey].items.add(newLevel)
                }
            }
            monkey.items.clear()
        }
    }
    println(monkeys.map { it.inspectionCount }.sortedDescending().take(2).reduce { acc, i -> acc * i })
}

fun main() {
    val input = loadFromResources("day11.txt").readLines().splitWhen { it.isBlank() }

    // part 1
    solve(input.map(::toMonkey).toList(), 20, 3)

    // part 2
    solve(input.map(::toMonkey).toList(), 10000, 1)
}

