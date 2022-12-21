package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day20.txt").readLines().map(String::toInt).mapIndexed { idx, it ->
        it to idx
    }

    // part 1
    val list = input.toMutableList()
    val indexes = mutableMapOf<Int, Int>()
    list.forEach { (_, i) ->
        indexes[i] = i
    }
    println(list)
    for (i in list.indices) {
        val idx = indexes[i]!!
        val (v, _) = list[idx]
        var newPos = (idx + v) % (list.size-1)
        if (newPos < 0) {
            newPos += list.size - 1
        }
        println("${v} moves from ${idx} to ${newPos}")
        if (idx != newPos) {
            indexes[i] = newPos
            if (newPos < idx) {
                for (j in newPos until idx) {
                    val tpl = list[j]
                    indexes[tpl.second] = j + 1
                }
            } else {
                for (j in idx + 1..newPos) {
                    val tpl = list[j]
                    indexes[tpl.second] = j - 1
                }
            }
            list.removeAt(idx)
            list.add(newPos, v to idx)
        }
//        println(list)
    }
    for (i in list.indices) {
        val (v, _) = list[i]
        if (v == 0) {
            println("0 is at index ${i}")
            val plus1000 = list[(i + 1000) % list.size]
            println("0 + 1000 is $plus1000")
            val plus2000 = list[(i + 2000) % list.size]
            println("0 + 2000 is $plus2000")
            val plus3000 = list[(i + 3000) % list.size]
            println("0 + 3000 is $plus3000")
            val s = plus1000.first + plus2000.first + plus3000.first
            println("sum is ${s}")
            break
        }
    }

}