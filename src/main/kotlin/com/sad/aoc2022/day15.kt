package com.sad.aoc2022

import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private fun toNumber(s: String): Int {
    val sb = StringBuilder()
    for (ch in s) {
        if (ch.isDigit() || ch == '-') {
            sb.append(ch)
        }
    }
    return sb.toString().toInt()
}

private fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>) =
    abs(p1.first - p2.first) + abs(p1.second - p2.second)

fun drawArea(grid: MutableList<Char>, sensor: Pair<Int, Int>, distance: Int, minx: Int, targety: Int) {
    if (targety in sensor.second - distance..sensor.second + distance) {
        for (x in sensor.first - distance..sensor.first + distance) {
            if (distance(sensor, x to targety) <= distance && grid[x - minx] == '.') {
                grid[x - minx] = '#'
            }
        }
    }
}

private fun part1(
    maxx: Int,
    minx: Int,
    sensors: List<Pair<Int, Int>>,
    beacons: List<Pair<Int, Int>>
) {
    val targety = 2000000

    val grid = MutableList(maxx - minx + 1) { '.' }
    for ((sensor, beacon) in sensors zip beacons) {
        if (sensor.second == targety) {
            grid[sensor.first - minx] = 'S'
        }
        if (beacon.second == targety) {
            grid[beacon.first - minx] = 'B'
        }
    }
    for ((sensor, beacon) in sensors zip beacons) {
        val distance = distance(sensor, beacon)
        drawArea(grid, sensor, distance, minx, targety)
    }

    println(grid.count { it == '#' })
}

private fun Iterable<IntRange>.union(): List<IntRange> = sortedBy { it.first }.fold(mutableListOf()) { acc, current ->
    if (acc.isEmpty()) {
        acc.add(current)
    } else {
        val last = acc.last()
        if (current.first <= last.last) {
            acc[acc.lastIndex] = last.first..maxOf(current.last, last.last)
        } else {
            acc.add(current)
        }
    }
    acc
}

private fun Iterable<IntRange>.findGapIndex(): Int? {
    var prev: IntRange? = null
    for (iv in this) {
        if (prev != null) {
            if (iv.first - prev.last >= 2) {
                return prev.last + 1
            }
        }
        prev = iv
    }
    return null
}

fun part2(sensors: List<Pair<Int, Int>>, beacons: List<Pair<Int, Int>>) {
    val maxDim = 4000000
//    val maxDim = 20
    val distances = (sensors zip beacons).map { (s, z) ->
        distance(s, z)
    }
    var t = System.currentTimeMillis()
    for (targety in 0..maxDim) {
        val intervals = mutableListOf<IntRange>()
        if (targety % 100 == 0) {
            val nt = System.currentTimeMillis()
            val diff = nt - t
            t = nt
            println("y = ${targety}, seconds = ${TimeUnit.MILLISECONDS.toSeconds(diff)}")
        }
        for (i in sensors.indices) {
            val sensor = sensors[i]
            val beacon = beacons[i]
            if (sensor.second == targety && sensor.first in 0..maxDim) {
                intervals += sensor.first..sensor.first
            }
            if (beacon.second == targety && beacon.first in 0..maxDim) {
                intervals += beacon.first..beacon.first
            }
        }
        for (i in sensors.indices) {
            val sensor = sensors[i]
            val distance = distances[i]
            if (targety in sensor.second - distance..sensor.second + distance) {
                val ydistance = abs(sensor.second - targety)
                intervals += max(sensor.first - distance + ydistance, 0)..min(sensor.first + distance - ydistance, maxDim)
            }
        }
        val gapIdx = intervals.union().findGapIndex()
        if (gapIdx != null) {
            println(gapIdx * 4000000L + targety)
            break
        }
    }
}

fun main() {
    val input = loadFromResources("day15.txt").readLines().map {
        val spl = it.split(" ")
        listOf(toNumber(spl[2]) to toNumber(spl[3]), toNumber(spl[8]) to toNumber(spl[9]))
    }
    val sensors = input.map { it[0] }
    val beacons = input.map { it[1] }
    var minx = Integer.MAX_VALUE
    var maxx = Integer.MIN_VALUE
    var miny = Integer.MAX_VALUE
    var maxy = Integer.MIN_VALUE
    for ((sensor, beacon) in sensors zip beacons) {
        minx = min(minx, sensor.first)
        minx = min(minx, beacon.first)
        maxx = max(maxx, sensor.first)
        maxx = max(maxx, beacon.first)

        miny = min(miny, sensor.second)
        miny = min(miny, beacon.second)
        maxy = max(maxy, sensor.second)
        maxy = max(maxy, beacon.second)

        val distance = distance(sensor, beacon)

        val farthestUp = sensor.first to sensor.second - distance
        val farthestDown = sensor.first to sensor.second + distance
        val farthestLeft = sensor.first - distance to sensor.second
        val farthestRight = sensor.first + distance to sensor.second

        minx = min(minx, farthestLeft.first)
        maxx = max(maxx, farthestRight.first)
        miny = min(miny, farthestUp.second)
        maxy = max(maxy, farthestDown.second)
    }
    println("minx: ${minx} maxx: ${maxx} miny: ${miny} maxy: ${maxy}")

    // part 1
//    part1(maxx, minx, sensors, beacons)

    // part 2
    part2(sensors, beacons)
}




