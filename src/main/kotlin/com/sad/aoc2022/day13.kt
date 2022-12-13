package com.sad.aoc2022

import java.lang.IllegalStateException

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

fun main() {
    val input = loadFromResources("day13.txt").readLines().splitWhen { it.isBlank() }.map {
        it.map { s -> parse(s) }
    }
//    println(input)
    for (l in input) {
        println(l[0])
        println(l[1])
        println("")
    }
}