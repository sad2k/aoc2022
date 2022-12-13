package com.sad.aoc2022

import java.lang.IllegalStateException
import kotlin.math.max

interface Packet

data class ListPacket(val list: MutableList<Packet>) : Packet

data class IntPacket(val value: Int) : Packet

private fun parse(s: String): Packet {
    var currentList: ListPacket? = null
    val stack = ArrayDeque<ListPacket>()
    val currentVal = StringBuilder()
    for (i in s.indices) {
        val ch = s[i]
        when (ch) {
            '[' -> {
                // new list starts
                if (currentList != null) {
                    stack.addFirst(currentList)
                }
                currentList = ListPacket(mutableListOf())
                currentVal.setLength(0)
            }
            ']' -> {
                // list finishes
                val topOfStack = stack.removeFirstOrNull()
                if (currentVal.isNotEmpty()) {
                    currentList!!.list.add(IntPacket(currentVal.toString().toInt()))
                    currentVal.setLength(0)
                }
                if (topOfStack == null) {
                    // this is actually a topmost list
                    return currentList!!
                } else {
                    topOfStack.list.add(currentList!!)
                    currentList = topOfStack
                }
            }
            ',' -> {
                if (currentVal.isNotEmpty()) {
                    val intPacket = IntPacket(currentVal.toString().toInt())
                    currentVal.setLength(0)
                    if (currentList != null) {
                        currentList.list.add(intPacket)
                    } else {
                        // the whole thing was just one value?
                        return intPacket
                    }
                }
            }
            ' ' -> {
                // ignore
            }
            else -> {
                currentVal.append(ch)
            }
        }
    }
    throw IllegalArgumentException("parsing error")
}

enum class CompareResult {
    IN_ORDER, OUT_OF_ORDER, CHECK_NEXT
}

private fun cmp(p1: Packet, p2: Packet): CompareResult {
    if (p1 is IntPacket && p2 is IntPacket) {
        if (p1.value < p2.value) {
            return CompareResult.IN_ORDER
        } else if (p1.value > p2.value) {
            return CompareResult.OUT_OF_ORDER
        } else {
            return CompareResult.CHECK_NEXT
        }
    } else if (p1 is IntPacket && p2 is ListPacket) {
        return cmp(ListPacket(mutableListOf(p1)), p2)
    } else if (p1 is ListPacket && p2 is IntPacket) {
        return cmp(p1, ListPacket(mutableListOf(p2)))
    } else if (p1 is ListPacket && p2 is ListPacket) {
        // the main check - two lists
        val maxNum = max(p1.list.size, p2.list.size)
        for (i in 0 until maxNum) {
            if (i >= p1.list.size) {
                // left ran out of items
                return CompareResult.IN_ORDER
            } else if (i >= p2.list.size) {
                // right ran out of items
                return CompareResult.OUT_OF_ORDER
            }
            when (cmp(p1.list[i], p2.list[i])) {
                CompareResult.IN_ORDER -> {
                    return CompareResult.IN_ORDER
                }
                CompareResult.OUT_OF_ORDER -> {
                    return CompareResult.OUT_OF_ORDER
                }
                CompareResult.CHECK_NEXT -> {
                    // just move on
                }
            }
        }
        return CompareResult.CHECK_NEXT
    } else {
        throw IllegalStateException("whats this then?")
    }
}

fun main() {
    // part 1
    val input = loadFromResources("day13.txt").readLines().splitWhen { it.isBlank() }.map {
        it.map { s -> parse(s) }
    }
    println(input.mapIndexed { i, it ->
        i to cmp(it[0], it[1])
    }.filter {
        it.second == CompareResult.IN_ORDER
    }.fold(0) { acc, v -> acc + v.first + 1 })

    // part 2
    val input2 = loadFromResources("day13.txt").readLines().filter { it.isNotBlank() }.map { s ->
        parse(s)
    } + listOf(
        ListPacket(mutableListOf(ListPacket(mutableListOf(IntPacket(2))))),
        ListPacket(mutableListOf(ListPacket(mutableListOf(IntPacket(6))))),
    )
    val sorted = input2.sortedWith(object : Comparator<Packet> {
        override fun compare(o1: Packet?, o2: Packet?): Int {
            when (cmp(o1!!, o2!!)) {
                CompareResult.IN_ORDER -> {
                    return -1
                }
                CompareResult.OUT_OF_ORDER -> {
                    return 1
                }
                CompareResult.CHECK_NEXT -> {
                    return 0
                }
            }
        }
    })
    var res = 1
    for ((i, s) in sorted.withIndex()) {
        if (s == ListPacket(mutableListOf(ListPacket(mutableListOf(IntPacket(2))))) ||
            s == ListPacket(mutableListOf(ListPacket(mutableListOf(IntPacket(6)))))
        ) {
            res *= (i + 1)
        }
    }
    println(res)
}