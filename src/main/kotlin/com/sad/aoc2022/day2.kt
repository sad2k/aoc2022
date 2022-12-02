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
            for (res in values()) {
                cache[res.alias] = res
            }
        }

        fun getByAlias(alias: Char): Result? = cache[alias]
    }
}

private val winMoves: Map<Shape, Shape> = mapOf(
    Shape.SCISSORS to Shape.ROCK,
    Shape.PAPER to Shape.SCISSORS,
    Shape.ROCK to Shape.PAPER
)

private val loseMoves = mutableMapOf<Shape, Shape>().apply {
    val thisMap = this
    winMoves.forEach { (k, v) -> thisMap[v] = k }
}

private fun result(otherShape: Shape, myShape: Shape): Result {
    if (myShape == otherShape) {
        return Result.DRAW
    } else {
        val winningShape = winMoves[otherShape]!!
        return if (myShape == winningShape) Result.WIN else Result.LOSE
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
            return winMoves[otherShape]!!
        } else {
            return loseMoves[otherShape]!!
        }
    }
}

private fun score2(otherShape: Shape, neededResult: Result): Int {
    return score(otherShape, chooseShape(otherShape, neededResult))
}

fun main() {
    // part 1
    val input = loadFromResources("day2.txt").readLines().map {
        it.split(' ').map { s -> Shape.getByAlias(s[0])!! }
    }
    println(input.map { score(it[0], it[1]) }.sum())

    // part 2
    val input2 = loadFromResources("day2.txt").readLines().map {
        val spl = it.split(' ')
        Pair(Shape.getByAlias(spl[0][0])!!, Result.getByAlias(spl[1][0])!!)
    }
    println(input2.map { score2(it.first, it.second) }.sum())
}