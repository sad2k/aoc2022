package com.sad.aoc2022

private fun contains(bigger: List<Int>, smaller: List<Int>): Boolean =
    smaller[0] >= bigger[0] && smaller[1] <= bigger[1]

fun main() {
    val input = loadFromResources("day4.txt").readLines().map {
        it.split(",").map { s ->
            s.split("-").map(String::toInt)
        }
    }

    // part 1
    println(input.map {
        (fst, snd) -> contains(fst, snd) || contains(snd, fst)
    }.count { it })

}