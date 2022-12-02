package com.sad.aoc2022

enum class Shape(val aliases: List<Char>, val score: Int) {
    ROCK(listOf('A', 'X'), 1), PAPER(listOf('B', 'Y'), 2), SCISSORS(listOf('C', 'Z'), 3);

    companion object {
        private val cache = mutableMapOf<Char, Shape>()

        init {
            for (shape in values()) {
                for (alias in shape.aliases) {
                    cache[alias] = shape
                }
            }
        }

        fun getByAlias(alias: Char): Shape? = cache[alias]
    }
}

enum class Result(val score: Int) {
    WIN(6), LOSE(0), DRAW(3)
}

private fun result(otherShape: Shape, myShape: Shape): Result {
    if (myShape == otherShape) {
        return Result.DRAW
    } else {
        if (myShape == Shape.ROCK && otherShape == Shape.SCISSORS) {
            return Result.WIN
        } else if (myShape == Shape.SCISSORS && otherShape == Shape.PAPER) {
            return Result.WIN
        } else if (myShape == Shape.PAPER && otherShape == Shape.ROCK) {
            return Result.WIN
        } else {
            return Result.LOSE
        }
    }
}

private fun score(otherShape: Shape, myShape: Shape): Int {
    return myShape.score + result(otherShape, myShape).score
}

fun main() {
    val input = loadFromResources("day2.txt").readLines().map {
        it.split(' ').map { s -> Shape.getByAlias(s[0])!! }
    }

    // part 1
    println(input.map { score(it[0], it[1]) }.sum())
}