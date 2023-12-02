fun processGame(game: String): List<List<Pair<String, Int>>> {
    return game.drop(game.indexOfFirst { it == ':' } + 2)
        .split(';')
        .map { set ->
            set.dropWhile { it == ' ' }
                .split(", ")
                .map { it.substringAfter(' ') to it.substringBefore(' ').toInt()}
        }
}

fun day2Part1(input: List<String>): Int {
    val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)
    return input.mapIndexed { idx, line ->
        var ok = true
        processGame(line)
            .forEach { it.forEach { pull -> if (limits[pull.first]!! < pull.second) ok = false } }
            .let { if (ok) idx + 1 else 0 }
    }.sum()
}

fun day2Part2(input: List<String>): Int {
    return input.sumOf { line ->
        val max = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
        processGame(line)
            .forEach { set ->
                set.forEach { pull ->
                    max.compute(pull.first) { _, v -> if (pull.second > v!!) pull.second else v }
                }
            }
            .let { max.values.reduce(Int::times) }
    }
}

fun main()
{
    val input = Input("input_day_2.txt").readLines()

    val part1 = day2Part1(input)
    println("Part 1 : $part1")

    val part2 = day2Part2(input)
    println("Part 2 : $part2")
}