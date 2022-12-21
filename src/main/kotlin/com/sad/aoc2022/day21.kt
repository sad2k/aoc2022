package com.sad.aoc2022

import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException

interface MathMonkey {
    fun getNumber(): Long

    fun dependsOnPlaceholder(): Boolean

    fun findPlaceholderValue(currentVal: Long): Long
}

data class NumberMonkey(private val number: Long) : MathMonkey {
    override fun getNumber(): Long = number

    override fun dependsOnPlaceholder(): Boolean = false

    override fun findPlaceholderValue(currentVal: Long): Long {
        throw UnsupportedOperationException()
    }
}

class PlaceholderMonkey : MathMonkey {
    override fun getNumber(): Long {
        throw UnsupportedOperationException()
    }

    override fun dependsOnPlaceholder(): Boolean = true

    override fun findPlaceholderValue(currentVal: Long): Long = currentVal
}

abstract class CompositeMonkey(val monkey1: String, val monkey2: String, val cache: Map<String, MathMonkey>) :
    MathMonkey {
    override fun dependsOnPlaceholder(): Boolean =
        cache[monkey1]!!.dependsOnPlaceholder() || cache[monkey2]!!.dependsOnPlaceholder()

    protected fun getPlaceholderDependentBranch(): MathMonkey {
        val m1 = cache[monkey1]!!
        if (m1.dependsOnPlaceholder()) {
            return m1
        } else {
            return cache[monkey2]!!
        }
    }

    protected fun getPlaceholderIndependentBranch(): MathMonkey {
        val m1 = cache[monkey1]!!
        if (m1.dependsOnPlaceholder()) {
            return cache[monkey2]!!
        } else {
            return m1
        }
    }
}

class PlusMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() + cache[monkey2]!!.getNumber()

    override fun findPlaceholderValue(currentVal: Long): Long {
        val indepValue = getPlaceholderIndependentBranch().getNumber()
        return getPlaceholderDependentBranch().findPlaceholderValue(currentVal - indepValue)
    }
}

class MinusMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() - cache[monkey2]!!.getNumber()

    override fun findPlaceholderValue(currentVal: Long): Long {
        val m1 = cache[monkey1]!!
        if (m1.dependsOnPlaceholder()) {
            return m1.findPlaceholderValue(currentVal + cache[monkey2]!!.getNumber())
        } else {
            return cache[monkey2]!!.findPlaceholderValue(m1.getNumber() - currentVal)
        }
    }
}

class MultMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() * cache[monkey2]!!.getNumber()

    override fun findPlaceholderValue(currentVal: Long): Long {
        val indepValue = getPlaceholderIndependentBranch().getNumber()
        return getPlaceholderDependentBranch().findPlaceholderValue(currentVal / indepValue)
    }
}

class DivMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long = cache[monkey1]!!.getNumber() / cache[monkey2]!!.getNumber()

    override fun findPlaceholderValue(currentVal: Long): Long {
        val m1 = cache[monkey1]!!
        if (m1.dependsOnPlaceholder()) {
            return m1.findPlaceholderValue(currentVal * cache[monkey2]!!.getNumber())
        } else {
            return cache[monkey2]!!.findPlaceholderValue(m1.getNumber() / currentVal)
        }
    }
}

class EqMonkey(monkey1: String, monkey2: String, cache: Map<String, MathMonkey>) :
    CompositeMonkey(monkey1, monkey2, cache) {
    override fun getNumber(): Long {
        throw UnsupportedOperationException()
    }

    override fun findPlaceholderValue(currentVal: Long): Long {
        val indepValue = getPlaceholderIndependentBranch().getNumber()
        return getPlaceholderDependentBranch().findPlaceholderValue(indepValue)
    }
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

fun part2(input: List<List<String>>) {
    val cache = mutableMapOf<String, MathMonkey>()
    input.forEach { (name, def) ->
        val monkey =
            if (name == "root") {
                // it's + in both example and test so will make it easier
                val spl = def.split("\\s*\\+\\s*".toRegex())
                EqMonkey(spl[0], spl[1], cache)
            } else if (name == "humn") {
                PlaceholderMonkey()
            } else if (def.all { it.isDigit() }) {
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
    println(cache["root"]!!.findPlaceholderValue(0))
}

fun main() {
    val input = loadFromResources("day21.txt").readLines().map {
        it.split(":\\s*".toRegex())
    }

    // part 1
//    part1(input)

    // part 2
    part2(input)
}



