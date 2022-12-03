package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day3.txt").readLines().map {
        val halfLength = it.length / 2
        it.substring(0, halfLength) to it.substring(halfLength)
    }

    // part 1
    println(input.map { (fst, snd) -> (fst.toSet() intersect snd.toSet()).first() }
        .map {
            if (it.isLowerCase()) {
                it.code - 'a'.code + 1
            } else {
                it.code - 'A'.code + 27
            }
        }.sum())
}