package com.sad.aoc2022

data class State(
    val oreRobots: Int = 0, val clayRobots: Int = 0, val obsRobots: Int = 0, val geodeRobots: Int = 0,
    val ore: Int = 0, val clay: Int = 0, val obs: Int = 0, val geode: Int = 0,
    val minsLeft: Int
)

data class Blueprint(
    var id: Int,
    var oreRobotCost: Int = -1,
    var clayRobotCost: Int = -1,
    var obsRobotCostOre: Int = -1,
    var obsRobotCostClay: Int = -1,
    var geodeRobotCostOre: Int = -1,
    var geodeRobotCostObs: Int = -1
) {
    val cache = mutableMapOf<State, Int>()

    val maxOreRobotsNeeded: Int by lazy { maxOf(oreRobotCost, clayRobotCost, obsRobotCostOre, geodeRobotCostObs) }
    val maxClayRobotsNeeded: Int by lazy { obsRobotCostClay }
    val maxObsRobotsNeeded: Int by lazy { geodeRobotCostObs }

    fun calculate0(state: State): Int {
        if (state.minsLeft == 0) {
            return state.geode
        }

        val updState = state.copy(
            ore = state.ore + state.oreRobots,
            clay = state.clay + state.clayRobots,
            obs = state.obs + state.obsRobots,
            geode = state.geode + state.geodeRobots,
            minsLeft = state.minsLeft - 1
        )

        val candidates = mutableSetOf<State>()

        // saving for ore robot
        if (state.ore < oreRobotCost && state.oreRobots > 0) {
            val needToWaitMins = Math.ceil((oreRobotCost - state.ore).toDouble() / state.oreRobots.toDouble()).toInt()
            if (state.minsLeft >= needToWaitMins) {
                candidates.add(
                    state.copy(
                        ore = state.ore + state.oreRobots * needToWaitMins,
                        clay = state.clay + state.clayRobots * needToWaitMins,
                        obs = state.obs + state.obsRobots * needToWaitMins,
                        geode = state.geode + state.geodeRobots * needToWaitMins,
                        minsLeft = state.minsLeft - needToWaitMins
                    )
                )
            }
        }

        // saving for clay robot
        if (state.ore < clayRobotCost && state.oreRobots > 0) {
            val needToWaitMins = Math.ceil((clayRobotCost - state.ore).toDouble() / state.oreRobots.toDouble()).toInt()
            if (state.minsLeft >= needToWaitMins) {
                candidates.add(
                    state.copy(
                        ore = state.ore + state.oreRobots * needToWaitMins,
                        clay = state.clay + state.clayRobots * needToWaitMins,
                        obs = state.obs + state.obsRobots * needToWaitMins,
                        geode = state.geode + state.geodeRobots * needToWaitMins,
                        minsLeft = state.minsLeft - needToWaitMins
                    )
                )
            }
        }

        // saving for obsidian robot
        if (state.ore < obsRobotCostOre && state.oreRobots > 0 && state.clay < obsRobotCostClay && state.clayRobots > 0) {
            val needToWaitMinsOre =
                Math.ceil((obsRobotCostOre - state.ore).toDouble() / state.oreRobots.toDouble()).toInt()
            val needToWaitMinsClay =
                Math.ceil((obsRobotCostClay - state.clay).toDouble() / state.clayRobots.toDouble()).toInt()
            val needToWaitMins = maxOf(needToWaitMinsOre, needToWaitMinsClay)
            if (state.minsLeft >= needToWaitMins) {
                candidates.add(
                    state.copy(
                        ore = state.ore + state.oreRobots * needToWaitMins,
                        clay = state.clay + state.clayRobots * needToWaitMins,
                        obs = state.obs + state.obsRobots * needToWaitMins,
                        geode = state.geode + state.geodeRobots * needToWaitMins,
                        minsLeft = state.minsLeft - needToWaitMins
                    )
                )
            }
        }

        // saving for geode robot
        if (state.ore < geodeRobotCostOre && state.oreRobots > 0 && state.obs < geodeRobotCostObs && state.obsRobots > 0) {
            val needToWaitMinsOre =
                Math.ceil((geodeRobotCostOre - state.ore).toDouble() / state.oreRobots.toDouble()).toInt()
            val needToWaitMinsObs =
                Math.ceil((geodeRobotCostObs - state.obs).toDouble() / state.obsRobots.toDouble()).toInt()
            val needToWaitMins = maxOf(needToWaitMinsOre, needToWaitMinsObs)
            if (state.minsLeft >= needToWaitMins) {
                candidates.add(
                    state.copy(
                        ore = state.ore + state.oreRobots * needToWaitMins,
                        clay = state.clay + state.clayRobots * needToWaitMins,
                        obs = state.obs + state.obsRobots * needToWaitMins,
                        geode = state.geode + state.geodeRobots * needToWaitMins,
                        minsLeft = state.minsLeft - needToWaitMins
                    )
                )
            }
        }

        if (state.ore >= oreRobotCost && updState.oreRobots < maxOreRobotsNeeded) {
            candidates.add(updState.copy(ore = updState.ore - oreRobotCost, oreRobots = updState.oreRobots + 1))
        }
        if (state.ore >= clayRobotCost && updState.clayRobots < maxClayRobotsNeeded) {
            candidates.add(updState.copy(ore = updState.ore - clayRobotCost, clayRobots = updState.clayRobots + 1))
        }
        if (state.ore >= obsRobotCostOre && state.clay >= obsRobotCostClay && updState.obsRobots < maxObsRobotsNeeded) {
            candidates.add(
                updState.copy(
                    ore = updState.ore - obsRobotCostOre,
                    clay = updState.clay - obsRobotCostClay,
                    obsRobots = updState.obsRobots + 1
                )
            )
        }
        if (state.ore >= geodeRobotCostOre && state.obs >= geodeRobotCostObs) {
            candidates.clear()
            candidates.add(
                updState.copy(
                    ore = updState.ore - geodeRobotCostOre,
                    obs = updState.obs - geodeRobotCostObs,
                    geodeRobots = updState.geodeRobots + 1
                )
            )
        }

        if (candidates.isEmpty()) {
            return state.geode
        }


        return candidates.map { calculate(it) }.maxOf { it }
    }

    fun calculate(state: State): Int {
        if (cache.containsKey(state)) {
            return cache[state]!!
        }

        val res = calculate0(state)
        cache[state] = res
        if (cache.size % 100000 == 0) {
//            println("cache size is ${cache.size}")
        }
        return res
    }
}

private fun part1(blueprints: MutableList<Blueprint>) {
    var qsum = 0
    blueprints.forEach {
        val res = it.calculate(State(oreRobots = 1, minsLeft = 24))
        val quality = res * it.id
        println("${it} -> ${res}, quality ${quality}")
        it.cache.clear()
        System.gc()
        qsum += quality
    }
    println("qsum ${qsum}")
}

fun part2(blueprints: MutableList<Blueprint>) {
    blueprints.take(3).forEach {
        val res = it.calculate(State(oreRobots = 1, minsLeft = 32))
        println("${it} -> ${res}")
        it.cache.clear()
        System.gc()
    }
}

fun main() {
    val input = loadFromResources("day19.txt").readLines()
    val blueprints = mutableListOf<Blueprint>()

    val oreRegex = """Each ore robot costs (\d+) ore""".toRegex()
    val clayRegex = """Each clay robot costs (\d+) ore""".toRegex()
    val obsRegex = """Each obsidian robot costs (\d+) ore and (\d+) clay""".toRegex()
    val geodeRegex = """Each geode robot costs (\d+) ore and (\d+) obsidian""".toRegex()

    var blueprint: Blueprint? = null
    var i = 0
    input.forEach {
        var line = it
        if (line.startsWith("Blueprint ")) {
            if (blueprint != null) {
                blueprints.add(blueprint!!)
            }
            blueprint = Blueprint(id = ++i)
            val spl = line.split(':')
            line = spl[1]
        }
        line = line.trim()
        val spl = line.split('.')
        spl.forEach { rule ->
            val oreMatch = oreRegex.find(rule)
            if (oreMatch != null) {
                val oreRobotCost = oreMatch.destructured.component1().toInt()
                blueprint!!.oreRobotCost = oreRobotCost
            }
            val clayMatch = clayRegex.find(rule)
            if (clayMatch != null) {
                val clayRobotCost = clayMatch.destructured.component1().toInt()
                blueprint!!.clayRobotCost = clayRobotCost
            }
            val obsMatch = obsRegex.find(rule)
            if (obsMatch != null) {
                val (obsRobotCostOre, obsRobotCostClay) = obsMatch.destructured
                blueprint!!.obsRobotCostOre = obsRobotCostOre.toInt()
                blueprint!!.obsRobotCostClay = obsRobotCostClay.toInt()
            }
            val geodeMatch = geodeRegex.find(rule)
            if (geodeMatch != null) {
                val (geodeRobotCostOre, geodeRobotCostObs) = geodeMatch.destructured
                blueprint!!.geodeRobotCostOre = geodeRobotCostOre.toInt()
                blueprint!!.geodeRobotCostObs = geodeRobotCostObs.toInt()
            }
        }
    }
    if (blueprint != null) {
        blueprints.add(blueprint!!)
    }

    // part 1
//    part1(blueprints)

    // part 2
    part2(blueprints)
}



