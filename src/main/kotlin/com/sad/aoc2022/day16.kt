package com.sad.aoc2022

import java.util.concurrent.TimeUnit

data class Valve(val id: Byte, val openableId: Byte, val flowRate: Int, val moves: List<String>)

fun <T> Collection<T>.powerset(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerset().let { it + it.map { it + first() } }
}

private fun part1(input: Map<String, Valve>) {
    val allValves = input.keys.toList()
    val positionFlowValves = input.entries.filter { it.value.flowRate > 0 }.map { it.key }
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
                    if (currentValveDef.flowRate > 0 && !currentOpen.contains(currentPos)) {
                        openScore = (minLeft - 1) * currentValveDef.flowRate.toLong() + previousDp[Pair(
                            currentPos,
                            currentOpen + currentPos
                        )]!!
                    }

                    var moveScore = 0L
                    for (move in currentValveDef.moves) {
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

private fun addValveToSet(s: Int, valveId: Int) : Int = s or (1 shl valveId)

private fun isValveSet(s: Int, valveId: Int): Boolean = (s and (1 shl valveId)) > 0

private fun encodeSet(input: Map<String, Valve>, s: Set<String>) : Int {
    var res = 0
    for(valveName in s) {
        val valve = input[valveName]!!
        res = addValveToSet(res, valve.openableId.toInt())
    }
    return res
}

private fun part2(input: Map<String, Valve>) {
    val allValves = input.keys.toList()
    val positionFlowValves = input.entries.filter { it.value.flowRate > 0 }.map { it.key }
    val positionFlowValveCombinations = positionFlowValves.powerset().toList()

//    var currentDp = mutableMapOf<Triple<Byte, Byte, Int>, Int>()
//    var previousDp = mutableMapOf<Triple<Byte, Byte, Int>, Int>()

    var currentDp = Array(allValves.size) { Array(allValves.size) { Array(positionFlowValveCombinations.size) { 0 } }  }
    var previousDp = Array(allValves.size) { Array(allValves.size) { Array(positionFlowValveCombinations.size) { 0 } }  }

    var t = System.currentTimeMillis()

    for (minLeft in 0..26) {
        previousDp = currentDp
        currentDp = Array(allValves.size) { Array(allValves.size) { Array(positionFlowValveCombinations.size) { 0 } }  }
        System.gc()
        val newT = System.currentTimeMillis()
        val delta = newT - t
        t = newT
        println("calculating minute ${minLeft}, t=${TimeUnit.MILLISECONDS.toSeconds(delta)} s")
        for (myPos in allValves) {
            println(myPos)
            for (elephantPos in allValves) {
//                println("myPos: ${myPos}, elephantPos: ${elephantPos}")
                val myCurrentValve = input[myPos]!!
                val elephantsCurrentValve = input[elephantPos]!!
                for (currentOpen in positionFlowValveCombinations) {
                    if (minLeft > 0) {
                        // calculate options
                        val myOptions = mutableListOf<Pair<String,String>>()
                        val elephantOptions = mutableListOf<Pair<String,String>>()

                        // my options
                        if (myCurrentValve.flowRate > 0 && !currentOpen.contains(myPos)) {
                            myOptions.add("OPEN" to myPos)
                        }
                        for (move in myCurrentValve.moves) {
                            myOptions.add("MOVE" to move)
                        }
                        // elephant's options
                        if (elephantsCurrentValve.flowRate > 0 && !currentOpen.contains(elephantPos)) {
                            elephantOptions.add("OPEN" to elephantPos)
                        }
                        for (move in elephantsCurrentValve.moves) {
                            elephantOptions.add("MOVE" to move)
                        }

                        // combine options
                        var bestScore = 0
                        for (myOpt in myOptions) {
                            for (elephantOpt in elephantOptions) {
                                if (myOpt.first == "OPEN" && myOpt == elephantOpt) {
                                    // can't both open the same valve
                                    continue;
                                }

                                var newOpen = currentOpen
                                var meAtThisStep: Int
                                var newMyPos = myPos
                                if (myOpt.first == "OPEN") {
                                    meAtThisStep = (minLeft - 1) * myCurrentValve.flowRate
                                    newOpen = newOpen + myPos
                                } else {
                                    meAtThisStep = 0
                                    newMyPos = myOpt.second
                                }
                                var elephantAtThisStep: Int
                                var newElephantPos = elephantPos
                                if (elephantOpt.first == "OPEN") {
                                    elephantAtThisStep = (minLeft - 1) * elephantsCurrentValve.flowRate
                                    newOpen = newOpen + elephantPos
                                } else {
                                    elephantAtThisStep = 0
                                    newElephantPos = elephantOpt.second
                                }

                                val score = meAtThisStep + elephantAtThisStep + previousDp[input[newMyPos]!!.id.toInt()][input[newElephantPos]!!.id.toInt()][encodeSet(input, newOpen)]
                                bestScore = maxOf(bestScore, score)
                            }
                        }
                        currentDp[input[myPos]!!.id.toInt()][input[elephantPos]!!.id.toInt()][encodeSet(input, currentOpen)] = bestScore
                    } else {
                        currentDp[input[myPos]!!.id.toInt()][input[elephantPos]!!.id.toInt()][encodeSet(input, currentOpen)] = 0
                    }
                }
            }
        }
    }
    println(currentDp[input["AA"]!!.id.toInt()][input["AA"]!!.id.toInt()][0])
}

fun main() {
    val regex = """Valve (\w+) has flow rate=(\d+); tunnel[s]? lead[s]? to valve[s]? (.+)""".toRegex()
    var openableId: Byte = 0
    val input = loadFromResources("day16.txt").readLines().mapIndexed { index, it ->
        val mr = regex.find(it)
        val (v, fr, lt) = mr!!.destructured
        v to Valve(index.toByte(), if (fr.toInt() > 0) openableId++ else -1, fr.toInt(), lt.split("\\s*,\\s*".toRegex()))
    }.toMap()

    // part 1
//    part1(input)

    // part 2
    part2(input)
}

