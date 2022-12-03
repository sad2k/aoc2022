package com.sad.aoc2022

private fun getPriority(item: Char) = if (item.isLowerCase()) {
    item.code - 'a'.code + 1
} else {
    item.code - 'A'.code + 27
}

fun main() {
    val input = loadFromResources("day3.txt").readLines()

    // part 1
    println(input.map {
        val halfLength = it.length / 2
        it.substring(0, halfLength) to it.substring(halfLength)
    }
        .map { (fst, snd) -> (fst.toSet() intersect snd.toSet()).first() }
        .map(::getPriority).sum())

    // part 2
    println(input.chunked(3).map {
        (it[0].toSet() intersect it[1].toSet()) intersect it[2].toSet()
    }.map { getPriority(it.first()) }.sum() )
}