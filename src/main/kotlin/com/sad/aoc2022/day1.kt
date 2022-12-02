package com.sad.aoc2022

fun main() {
    val input = loadFromResources("day1.txt").readLines().splitWhen { it.isBlank() }.map {
        it.map(String::toLong)
    }

    // part 1
    println(input.map { it.sum() }.maxOf(Long::toDouble).toLong())

    // part 2
    println(input.map { it.sum() }.sortedDescending().take(3).sum())
}