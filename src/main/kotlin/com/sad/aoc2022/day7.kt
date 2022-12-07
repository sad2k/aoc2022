package com.sad.aoc2022

data class File(val name: String, val size: Long)

data class Dir(val name: String, val subdirs: MutableMap<String, Dir>, val files: MutableMap<String, File>) {

    val size: Long
        get() {
            var res = 0L
            for (f in files.values) {
                res += f.size
            }
            for (d in subdirs.values) {
                res += d.size
            }
            return res
        }

}

private fun process(input: List<String>): Map<String,Dir> {
    val dirs = mutableMapOf<String, Dir>()
    var currentPath = listOf("/")
    var currentDir: Dir? = null
    for (line in input) {
        if (line.startsWith("$")) {
            val cmd = line.substring(2)
            if (cmd.startsWith("cd")) {
                val cdArg = cmd.substring(3)
                when (cdArg) {
                    "/" -> currentPath = listOf("/")
                    ".." -> currentPath = currentPath.dropLast(1)
                    else -> currentPath = currentPath + cdArg
                }
                currentDir = dirs.computeIfAbsent(currentPath.toString()) {
                    Dir(it, mutableMapOf(), mutableMapOf())
                }
            }
        } else {
            val (fst, snd) = line.split(" ")
            if (fst == "dir") {
                val subDirPath = currentPath + snd
                val subDir = dirs.computeIfAbsent(subDirPath.toString()) {
                    Dir(it, mutableMapOf(), mutableMapOf())
                }
                currentDir!!.subdirs[snd] = subDir
            } else {
                val sz = fst.toLong()
                currentDir!!.files[snd] = File(snd, sz)
            }
        }
    }
    return dirs
}

fun main() {
    val input = loadFromResources("day7.txt").readLines()

    // part 1
    val dirs = process(input)
    var res = 0L
    for(d in dirs.values) {
//        println("${d} -> ${d.size}")
        if (d.size <= 100000) {
            res += d.size
        }
    }
    println(res)
}