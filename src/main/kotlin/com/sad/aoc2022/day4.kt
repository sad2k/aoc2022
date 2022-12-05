package com.sad.aoc2022

private fun contains(bigger: List<Int>, smaller: List<Int>): Boolean =
    smaller[0] >= bigger[0] && smaller[1] <= bigger[1]

private fun overlaps(fst: List<Int>, snd: List<Int>): Boolean =
    (fst[0] >= snd[0] && fst[0] <= snd[1]) ||
            (fst[1] >= snd[0] && fst[1] <= snd[1])

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

    // part 2
    println(input.map {
            (fst, snd) -> overlaps(fst, snd) || overlaps(snd, fst)
    }.count { it })
}