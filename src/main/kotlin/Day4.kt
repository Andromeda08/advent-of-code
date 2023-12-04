import kotlin.math.pow

data class Card(val id: Int, val key: List<Int>, val ans: List<Int>, val matches: Int)

fun getCards(input: List<String>): List<Card> {
    return input.map { card ->
        val (a, b) = card.drop(card.indexOfFirst { it == ':' } + 2).split(" | ")
        val key = a.windowed(2, 3).map { it.trim().toInt() }.toSet()
        val ans = b.windowed(2, 3).map { it.trim().toInt() }
        var matches = 0
        ans.forEach { if (it in key) matches++ }
        Card(card.drop(5).split(':')[0].trim().toInt(), key.toList(), ans, matches)
    }
}

fun day4Part1(input: List<String>): Int {
    return getCards(input).sumOf { 2.0.pow(it.matches - 1).toInt() }
}

fun day4Part2(input: List<String>): Int {
    val cards = getCards(input)
    val instances = cards.associate { card -> card.id to 1 }.toMutableMap()
    cards.forEach { card ->
        for (i in (card.id + 1)..((card.id + card.matches))) {
            instances[i] = (instances[i] ?: 1) + (instances[card.id] ?: 1)
        }
    }
    return instances.values.reduce(Int::plus)
}

fun main() {
    val input = Input("input_day_4.txt").readLines()

    println("Part 1 : ${day4Part1(input)}")
    println("Part 2 : ${day4Part2(input)}")
}