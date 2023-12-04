import kotlin.math.pow

data class Card(val id: Int, val key: List<Int>, val ans: List<Int>, val matches: Int)

fun day4Part1(input: List<String>): Int {
    return input.sumOf { card ->
        val (a, b) = card.drop(card.indexOfFirst { it == ':' } + 2).split(" | ")
        val ans = a.windowed(2, 3).map { it.trim().toInt() }.toSet()
        var res = -1
        b.windowed(2, 3).map { it.trim().toInt() }.forEach { if (it in ans) res++ }
        2.0.pow(res).toInt()
    }
}

fun getCards(input: List<String>): List<Card> {
    return input.map { card ->
        val (id, nums) = card.drop(5).split(':')
        val (key, ans) = nums.split(" | ").map { str ->
            str.trim().windowed(2, 3).map { it.trim().toInt() }
        }
        val matches = mutableListOf<Int>()
        ans.forEach { if (it in key) matches.add(it) }
        Card(id.trim().toInt(), key, ans, matches.count())
    }
}

fun day4Part2(input: List<String>): Int {
    val cards = getCards(input).forEach {println(it)}

    return 0
}

fun main() {
    val input = Input("input_day_4.txt").readLines()

    println("Part 1 : ${day4Part1(input)}")
    println("Part 2 : ${day4Part2(input)}")
}