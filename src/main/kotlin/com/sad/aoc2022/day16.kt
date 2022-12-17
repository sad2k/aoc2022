package com.sad.aoc2022

import java.util.concurrent.TimeUnit

fun <T> Collection<T>.powerset(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerset().let { it + it.map { it + first() } }
}

private fun part1(input: Map<String, Pair<Int, List<String>>>) {
    val allValves = input.keys.toList()
    val positionFlowValves = input.entries.filter { it.value.first > 0 }.map { it.key }
    val positionFlowValveCombinations = positionFlowValves.powerset().toList()

    var currentDp = mutableMapOf<Pair<String, Set<String>>, Long>()
    var previousDp = mutableMapOf<Pair<String, Set<String>>, Long>()

    var t = System.currentTimeMillis()

    for (minLeft in 0..30) {
        previousDp = currentDp
        currentDp = mutableMapOf()
        val newT = System.currentTimeMillis()
        val delta = newT - t
        t = newT
        println("calculating minute ${minLeft}, t=${TimeUnit.MILLISECONDS.toSeconds(delta)} s")
        val minMap = currentDp
        for (currentPos in allValves) {
            val currentValveDef = input[currentPos]!!
            for (currentOpen in positionFlowValveCombinations) {
                if (minLeft > 0) {
                    var openScore = 0L
                    if (currentValveDef.first > 0 && !currentOpen.contains(currentPos)) {
                        openScore = (minLeft - 1) * currentValveDef.first.toLong() + previousDp[Pair(
                            currentPos,
                            currentOpen + currentPos
                        )]!!
                    }

                    var moveScore = 0L
                    for (move in currentValveDef.second) {
                        moveScore = maxOf(moveScore, previousDp[Pair(move, currentOpen)]!!)
                    }

                    minMap[Pair(currentPos, currentOpen)] = maxOf(openScore, moveScore)
                } else {
                    minMap[Pair(currentPos, currentOpen)] = 0
                }
            }
        }
    }
    println(currentDp[Pair("AA", emptySet())])
}

fun main() {
    val regex = """Valve (\w+) has flow rate=(\d+); tunnel[s]? lead[s]? to valve[s]? (.+)""".toRegex()
    val input = loadFromResources("day16.txt").readLines().map {
        val mr = regex.find(it)
        val (v, fr, lt) = mr!!.destructured
        v to Pair(fr.toInt(), lt.split("\\s*,\\s*".toRegex()))
    }.toMap()

    // part 1
    part1(input)
}

