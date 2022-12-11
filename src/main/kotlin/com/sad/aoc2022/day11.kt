package com.sad.aoc2022

data class Monkey(
    val items: MutableList<Int>, val op: Char, val leftOp: Int, val rightOp: Int, val divisibleBy: Int,
    val trueMonkey: Int, val falseMonkey: Int
)

private fun toMonkey(lines: List<String>): Monkey {
    var startingItems = mutableListOf<Int>()
    var op = '?'
    var leftOp = -1
    var rightOp = -1
    var divisibleBy = -1
    var trueMonkey = -1
    var falseMonkey = -1
    for ((i, line) in lines.withIndex()) {
        if (i > 0) {
            val spl = line.split(":")
            if (i == 1) {
                // starting items
                startingItems.addAll(spl[1].split(",").map(String::trim).map(String::toInt))
            } else if (i == 2) {
                // operation
                val spl2 = spl[1].split("=")[1].trim().split(" ")
                op = spl2[1].trim()[0]
                leftOp = if (spl2[0] == "old") -1 else spl2[0].toInt()
                rightOp = if (spl2[2] == "old") -1 else spl2[2].toInt()
            } else if (i == 3) {
                // divisible by
                val spl2 = spl[1].split(" ")
                divisibleBy = spl2[spl2.size - 1].toInt()
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
    return Monkey(startingItems, op, leftOp, rightOp, divisibleBy, trueMonkey, falseMonkey)
}

fun main() {
    val input = loadFromResources("day11.txt").readLines().splitWhen { it.isBlank() }
    val monkeys = input.map(::toMonkey).toList()

    // part 1
    val inspectedCounts = monkeys.map { 0 }.toMutableList()
    for (round in 1..20) {
        for ((monkeyIdx, monkey) in monkeys.withIndex()) {
            for (item in monkey.items) {
                inspectedCounts[monkeyIdx]++
                // new level
                val left = if (monkey.leftOp == -1) item else monkey.leftOp
                val right = if (monkey.rightOp == -1) item else monkey.rightOp
                var newLevel = if (monkey.op == '*') left * right else left + right
                newLevel /= 3
                if (newLevel % monkey.divisibleBy == 0) {
                    monkeys[monkey.trueMonkey].items.add(newLevel)
                } else {
                    monkeys[monkey.falseMonkey].items.add(newLevel)
                }
            }
            monkey.items.clear()
        }
    }
    println(inspectedCounts.sortedDescending().take(2).fold(1) { acc, i -> acc * i })
}