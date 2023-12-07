import kotlin.math.min
data class Hand(val hand: String, val bet: Int)
data class BucketElement(val idx: Int, val numbers: List<Int>)

fun getHandType(hand: String): Int {
    val cards = mutableMapOf<Char, Int>()
    hand.forEach { card ->
        cards[card] = cards[card]?.plus(1) ?: 1
    }

    val pairs = cards.count { (_, v) -> v == 2 }
    val threes = cards.count { (_, v) -> v == 3 }
    val fours = cards.count { (_, v) -> v == 4 }

    val isTwoPair = pairs == 2
    val isFiveOfAKind = cards.count() == 1
    val isFullHouse = pairs == 1 && threes == 1

    if (isFiveOfAKind) return 1
    if (fours == 1) return 2
    if (isFullHouse) return 3
    if (threes == 1) return 4
    if (isTwoPair) return 5
    if (pairs == 1) return 6
    return 7
}

fun getBestHandType(hand: String): Int {
    var bestCategory = getHandType(hand)
    if (!hand.contains("J")) { return bestCategory }

    val uniqueCards = hand.toSet()
    val idxJ = mutableListOf<Int>()
    hand.forEachIndexed { i, c -> if (c == 'J') idxJ.add(i) }

    var hands = listOf(hand)
    idxJ.forEach { j ->
        val new = hands.toMutableList()
        hands.forEach { h ->
            uniqueCards.forEach { c ->
                if (c != 'J') {
                    new.add("${h.take(j)}$c${h.takeLast(h.length - j - 1)}")
                }
            }
        }
        hands = new.toList()
    }
    hands.forEach { bestCategory = min(bestCategory, getHandType(it)) }

    return bestCategory
}

fun bucketByType(hands: List<Hand>, withJokers: Boolean = false): Map<Int, List<Hand>> {
    val buckets = mutableMapOf<Int, MutableList<Hand>>()
    (1..7).forEach{buckets[it] = mutableListOf() }

    hands.forEach { hand ->
        val handType = if (withJokers) getBestHandType(hand.hand) else getHandType(hand.hand)
        buckets[handType]?.add(hand)
    }

    return buckets
}

fun compareBucketElement(a: BucketElement, b: BucketElement): Int {
    val listA = a.numbers;
    val listB = b.numbers;

    var i = 0
    while (listA[i] == listB[i]) {
        if (i == a.numbers.count()) return 0
        i++
    }

    return listB[i].compareTo(listA[i])
}

fun rankBucketElements(hands: List<Hand>, cardValues: Map<Char, Int>): List<Hand> {
    return hands.mapIndexed { idx, it ->
        BucketElement(idx, it.hand.map { c -> cardValues[c] ?: 0 })
    }
        .sortedWith { a, b -> compareBucketElement(a, b) }
        .map { hands[it.idx] }
}

fun solve(hands: List<Hand>, cardValues: Map<Char, Int>, jokers: Boolean): Int {
    return bucketByType(hands, jokers)
        .flatMap { (_, v) -> rankBucketElements(v, cardValues) }
        .mapIndexed { idx, hand -> hand.bet * (hands.count() - idx) }
        .sum()
}
fun day7part1(hands: List<Hand>): Int {
    val cardValues = mapOf(
        '2' to 0, '3' to 1, '4' to 2, '5' to 3, '6' to 4, '7' to 5,
        '8' to 6, '9' to 7, 'T' to 8, 'J' to 9, 'Q' to 10, 'K' to 11,
        'A' to 12
    )
    return solve(hands, cardValues, false)
}

fun day7part2(hands: List<Hand>): Int {
    val cardValues = mapOf(
        'J' to 0,
        '2' to 1, '3' to 2, '4' to 3, '5' to 4, '6' to 5, '7' to 6,
        '8' to 7, '9' to 8, 'T' to 9, 'Q' to 10, 'K' to 11, 'A' to 12
    )
    return solve(hands, cardValues, true)
}

fun main() {
    val hands = Input("input_day_7.txt").readLines()
        .map {
            val (cards, bet) = it.split(" ")
            Hand(cards, bet.toInt())
        }

    println("Part 1 : ${day7part1(hands)}")
    println("Part 2 : ${day7part2(hands)}")
}