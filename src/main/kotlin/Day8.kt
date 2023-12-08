data class LR(val left: String, val right: String)

fun getStepCount(root: String, map: Map<String, LR>, dirs: List<String>): Int {
    var i = 0
    var steps = 0
    var node = root
    while (!node.endsWith("Z")) {
        node = if (dirs[i] == "R") {
            map[node]?.right!!
        } else {
            map[node]?.left!!
        }
        steps++
        i = (i + 1).mod(dirs.count())
    }
    return steps
}

fun day8part2(map: Map<String, LR>, dirs: List<String>): String {
    return "LCM of : ${map.keys.filter { it.endsWith("A") }
        .map { getStepCount(it, map, dirs).toLong() }}"
}

fun main() {
    val input = Input("input_day_8.txt").readLines()
    val instructions = input.first().toCharArray().map{it.toString()}
    val map = input.drop(2).associate { line ->
        val (k, p) = line.split("=")
        val (a, b) = p.trim().subSequence(1, p.length - 2).split(',').map{it.trim()}
        k.trim() to LR(a, b)
    }

    println("Part 1 : ${getStepCount("AAA", map, instructions)}")
    println("Part 2 : ${day8part2(map, instructions)}")
}
