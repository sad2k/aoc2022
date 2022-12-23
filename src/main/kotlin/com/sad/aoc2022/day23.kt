package com.sad.aoc2022

private fun northChecker(coord: Pair<Int, Int>, elfs: Map<Pair<Int, Int>, Int>): Pair<Int, Int>? {
    val n = coord.first - 1 to coord.second
    val ne = coord.first - 1 to coord.second + 1
    val nw = coord.first - 1 to coord.second - 1
    if (n !in elfs && ne !in elfs && nw !in elfs) {
        return n
    } else {
        return null
    }
}

private fun southChecker(coord: Pair<Int, Int>, elfs: Map<Pair<Int, Int>, Int>): Pair<Int, Int>? {
    val s = coord.first + 1 to coord.second
    val se = coord.first + 1 to coord.second + 1
    val sw = coord.first + 1 to coord.second - 1
    if (s !in elfs && se !in elfs && sw !in elfs) {
        return s
    } else {
        return null
    }
}

private fun westChecker(coord: Pair<Int, Int>, elfs: Map<Pair<Int, Int>, Int>): Pair<Int, Int>? {
    val w = coord.first to coord.second - 1
    val nw = coord.first - 1 to coord.second - 1
    val sw = coord.first + 1 to coord.second - 1
    if (w !in elfs && nw !in elfs && sw !in elfs) {
        return w
    } else {
        return null
    }
}

private fun eastChecker(coord: Pair<Int, Int>, elfs: Map<Pair<Int, Int>, Int>): Pair<Int, Int>? {
    val e = coord.first to coord.second + 1
    val ne = coord.first - 1 to coord.second + 1
    val se = coord.first + 1 to coord.second + 1
    if (e !in elfs && ne !in elfs && se !in elfs) {
        return e
    } else {
        return null
    }
}

private fun drawMap(coords: List<Pair<Int, Int>>, minCoord: Pair<Int, Int>, maxCoord: Pair<Int, Int>) {
    for (i in minCoord.first..maxCoord.first) {
        val sb = StringBuilder()
        for (j in minCoord.second..maxCoord.second) {
            if (i to j in coords) {
                sb.append('#')
            } else {
                sb.append('.')
            }
        }
        println(sb)
    }
}

private fun findBoundaries(coords: List<Pair<Int, Int>>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    var minRow = Integer.MAX_VALUE
    var minCol = Integer.MAX_VALUE
    var maxRow = Integer.MIN_VALUE
    var maxCol = Integer.MIN_VALUE
    coords.forEach { (row, col) ->
        minRow = minOf(minRow, row)
        minCol = minOf(minCol, col)
        maxRow = maxOf(maxRow, row)
        maxCol = maxOf(maxCol, col)
    }
    return Pair(Pair(minRow, minCol), Pair(maxRow, maxCol))
}

fun main() {
    val input = loadFromResources("day23.txt").readLines()

    val elfToCoord = mutableMapOf<Int, Pair<Int, Int>>()
    val coordToElf = mutableMapOf<Pair<Int, Int>, Int>()
    var elfId = 0
    for (i in input.indices) {
        val s = input[i]
        for (j in s.indices) {
            if (s[j] == '#') {
                val coord = i to j
                elfId++
                elfToCoord[elfId] = coord
                coordToElf[coord] = elfId
            }
        }
    }

    // part 1
    part1(elfToCoord, coordToElf)
}

private fun part1(
    elfToCoord: MutableMap<Int, Pair<Int, Int>>,
    coordToElf: MutableMap<Pair<Int, Int>, Int>
) {
    val coordCheckers = mutableListOf(
        ::northChecker,
        ::southChecker,
        ::westChecker,
        ::eastChecker
    )
    repeat(10) {
        val proposals = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        for ((elf, coord) in elfToCoord.entries) {
            val (row, col) = coord
            if (row - 1 to col in coordToElf ||
                row - 1 to col - 1 in coordToElf ||
                row - 1 to col + 1 in coordToElf ||
                row + 1 to col in coordToElf ||
                row + 1 to col - 1 in coordToElf ||
                row + 1 to col + 1 in coordToElf ||
                row to col - 1 in coordToElf ||
                row to col + 1 in coordToElf
            ) {
                for (checker in coordCheckers) {
                    val newCoord = checker(coord, coordToElf)
                    if (newCoord != null) {
                        proposals.computeIfAbsent(newCoord) {
                            mutableListOf()
                        }.add(elf)
                        break
                    }
                }
            }
        }
        proposals.filter { (_, elfs) -> elfs.size == 1 }.forEach { (coord, elfs) ->
            val elf = elfs[0]
            val oldCoord = elfToCoord.remove(elf)!!
            check(coordToElf.remove(oldCoord) != null)
            elfToCoord[elf] = coord
            coordToElf[coord] = elf
        }

        coordCheckers.add(coordCheckers.removeAt(0))
    }

    val (minPair, maxPair) = findBoundaries(coordToElf.keys.toList())
    val (minRow, minCol) = minPair
    val (maxRow, maxCol) = maxPair
//    drawMap(coordToElf.keys.toList(), minPair, maxPair)
    println("min ${minRow},${minCol}")
    println("max ${maxRow},${maxCol}")
    var empty = 0
    for (row in minRow..maxRow) {
        for (col in minCol..maxCol) {
            if (row to col !in coordToElf) {
                empty++
            }
        }
    }
    println("empty ${empty}")
}