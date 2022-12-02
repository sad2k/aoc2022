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

enum class Result(val score: Int, val alias: Char) {
    WIN(6, 'Z'), LOSE(0, 'X'), DRAW(3, 'Y');

    companion object {
        private val cache = mutableMapOf<Char, Result>()

        init {
            for (res in Result.values()) {
                cache[res.alias] = res
            }
        }

        fun getByAlias(alias: Char): Result? = cache[alias]
    }
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

private fun chooseShape(otherShape: Shape, neededResult: Result): Shape {
    if (neededResult == Result.DRAW) {
        return otherShape
    } else {
        if (neededResult == Result.WIN) {
            return when (otherShape) {
                Shape.ROCK -> Shape.PAPER
                Shape.SCISSORS -> Shape.ROCK
                Shape.PAPER -> Shape.SCISSORS
            }
        } else {
            return when (otherShape) {
                Shape.PAPER -> Shape.ROCK
                Shape.ROCK -> Shape.SCISSORS
                Shape.SCISSORS -> Shape.PAPER
            }
        }
    }
}

private fun score2(otherShape: Shape, neededResult: Result): Int {
    return score(otherShape, chooseShape(otherShape, neededResult))
}

fun main() {
    val input = loadFromResources("day2.txt").readLines().map {
        it.split(' ').map { s -> Shape.getByAlias(s[0])!! }
    }

    // part 1
    println(input.map { score(it[0], it[1]) }.sum())

    // part 2
    val input2 = loadFromResources("day2.txt").readLines().map {
        val spl = it.split(' ')
        Pair(Shape.getByAlias(spl[0][0])!!, Result.getByAlias(spl[1][0])!!)
    }
    println(input2.map { score2(it.first, it.second) }.sum())
}