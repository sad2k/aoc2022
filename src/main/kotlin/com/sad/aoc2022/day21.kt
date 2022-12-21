package com.sad.aoc2022

import java.lang.IllegalArgumentException

interface MathMonkey {
    fun getNumber(): Long
}

data class NumberMonkey(private val number: Long) : MathMonkey {
    override fun getNumber(): Long = number
}

abstract class CompositeMonkey(val monkey1: String, val monkey2: String, val cache: Map<String, MathMonkey>) :
    MathMonkey

class PlusMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() + cache[monkey2]!!.getNumber()
}

class MinusMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() - cache[monkey2]!!.getNumber()
}

class MultMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() * cache[monkey2]!!.getNumber()
}

class DivMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() / cache[monkey2]!!.getNumber()
}

private fun part1(
    input: List<List<String>>
) {
    val cache = mutableMapOf<String, MathMonkey>()
    input.forEach { (name, def) ->
        val monkey =
            if (def.all { it.isDigit() }) {
                NumberMonkey(def.toLong())
            } else if ('+' in def) {
                val spl = def.split("\\s*\\+\\s*".toRegex())
                PlusMonkey(spl[0], spl[1], cache)
            } else if ('-' in def) {
                val spl = def.split("\\s*\\-\\s*".toRegex())
                MinusMonkey(spl[0], spl[1], cache)
            } else if ('*' in def) {
                val spl = def.split("\\s*\\*\\s*".toRegex())
                MultMonkey(spl[0], spl[1], cache)
            } else if ('/' in def) {
                val spl = def.split("\\s*\\/\\s*".toRegex())
                DivMonkey(spl[0], spl[1], cache)
            } else {
                throw IllegalArgumentException(def)
            }
        cache[name] = monkey
    }
    println(cache["root"]!!.getNumber())
}

fun main() {
    val input = loadFromResources("day21.txt").readLines().map {
        it.split(":\\s*".toRegex())
    }
    part1(input)
}

