package com.sad.aoc2022

import java.lang.IllegalStateException
import kotlin.math.max
import kotlin.math.min

private fun fill(grid: MutableList<MutableList<Char>>, start: List<Int>, end: List<Int>, minx: Int) {
    val miny = 0
    if (start[0] == end[0]) {
        // y varies
        val x = start[0]
        val smaller = min(start[1], end[1])
        val larger = max(start[1], end[1])
        for (y in smaller..larger) {
            grid[y - miny][x - minx] = '#'
        }
    } else if (start[1] == end[1]) {
        val y = start[1]
        val smaller = min(start[0], end[0])
        val larger = max(start[0], end[0])
        for (x in smaller..larger) {
            grid[y - miny][x - minx] = '#'
        }
        // x varies
    } else {
        throw IllegalArgumentException("bad pair: ${start} and ${end}")
    }
}

private fun checkPosition(grid: MutableList<MutableList<Char>>, pos: List<Int>): Char {
    val (x,y) = pos
    if (x < 0 || x >= grid[0].size || y == grid.size) {
        return '.' // assume everything below the grid is unoccupied
    }
    return grid[y][x]
}

private fun cycle(grid: MutableList<MutableList<Char>>, pos: List<Int>, allowOutside: Boolean): Boolean {
    var (x, y) = pos
    while (true) {
        if (x < 0 || x >= grid[0].size || y == grid.size) {
            if (!allowOutside) {
                throw IllegalStateException("not allowed to go outside the grid")
            }
            return false
        }
        if (checkPosition(grid, listOf(x,y+1)) == '.') {
            y++
        } else if (checkPosition(grid, listOf(x-1,y+1)) == '.') {
            y++
            x--
        } else if (checkPosition(grid, listOf(x+1,y+1)) == '.') {
            y++
            x++
        } else {
            grid[y][x] = 'o'
            return true
        }
    }
}

private fun printGrid(grid: MutableList<MutableList<Char>>) {
    for (gridRow in grid) {
        println(gridRow.joinToString(separator = ""))
    }
}

private fun part1(input: List<List<List<Int>>>) {
    var minx = Integer.MAX_VALUE
    var miny = 0
    var maxx = 0
    var maxy = 0
    input.forEach {
        it.forEach { (x, y) ->
            minx = min(x, minx)
            maxx = max(x, maxx)
            maxy = max(y, maxy)
        }
    }
//    println("min: ${minx},${miny} max: ${maxx},${maxy}")
    val grid = MutableList(maxy - miny + 1) { MutableList(maxx - minx + 1) { '.' } }
    input.forEach {
        it.windowed(2, 1).map { (start, end) ->
            fill(grid, start, end, minx)
        }
    }
//    printGrid(grid)
    val fallStart = listOf(500 - minx, 0)
    var cycles = 0
    while (cycle(grid, fallStart, true)) {
        cycles++
    }
//    printGrid(grid)
    println(cycles)
}

private fun part2(input: List<List<List<Int>>>) {
    var minx = Integer.MAX_VALUE
    var miny = 0
    var maxx = 0
    var maxy = 0
    input.forEach {
        it.forEach { (x, y) ->
            minx = min(x, minx)
            maxx = max(x, maxx)
            maxy = max(y, maxy)
        }
    }
    maxy += 2
    minx = 500 - (maxy - miny)
    maxx = 500 + (maxy - miny)
    val grid = MutableList(maxy - miny + 1) { MutableList(maxx - minx + 1) { '.' } }
    input.forEach {
        it.windowed(2, 1).map { (start, end) ->
            fill(grid, start, end, minx)
        }
    }
    fill(grid, listOf(minx, maxy), listOf(maxx, maxy), minx)
//    printGrid(grid)
    val fallStart = listOf(500 - minx, 0)
    var cycles = 0
    while (cycle(grid, fallStart, true)) {
        cycles++
        if (grid[fallStart[1]][fallStart[0]] == 'o') {
            break
        }
    }
//    printGrid(grid)
    println(cycles)
}

fun main() {
    val input = loadFromResources("day14.txt").readLines().map {
        it.split(" -> ").map { pair ->
            pair.split(",").map { s -> s.toInt() }
        }
    }

    // part 1
    part1(input)

    // part 2
    part2(input)
}







