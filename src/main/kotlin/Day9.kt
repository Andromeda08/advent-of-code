fun getValueIncrease(numbers: List<Int>): MutableList<Int> {
    return numbers.windowed(2, 1).map { w ->
        val (a, b) = w
        b - a
    }.toMutableList()
}

fun getNextValue(last: List<Int>, current: List<Int>) : Int {
    if (current.all{it == 0}) return 0
    return last.last() + current.last()
}

fun getPrevValue(a: List<Int>, b: List<Int>) : Int {
    if (b.all{it == 0}) return 0
    return b.last() - a.last()
}


fun day9part1(input: List<List<Int>>): Int {
    return input.map { set ->
        val list = mutableListOf(set.toMutableList())
        while (!list.last().all{n -> n == 0}) {
            val values = getValueIncrease(list.last())
            list.add(values)
        }
        list.add(emptyList<Int>().toMutableList())
        list.reversed().windowed(2, 1).map {
            val (last, curr) = it
            curr.add(getNextValue(last, curr))
            curr
        }.flatten()
    }.sumOf { it.last() }
}

fun day9part2(input: List<List<Int>>): Int {
    return input.map { set ->
        var list = mutableListOf(set.toMutableList())
        while (!list.last().all { n -> n == 0 }) {
            val values = getValueIncrease(list.last())
            list.add(values)
        }
        list = list.reversed().toMutableList()
        list.map { s -> s.reversed().toMutableList() }
            .windowed(2, 1)
            .map {
                val (last, curr) = it
                curr.add(getPrevValue(last, curr))
                curr
            }.flatten()
    }.sumOf { it.last() }
}

fun main() {
    val input = Input("input_day_9.txt").readLines()
        .map { it.split(" ").map{n -> n.toInt()} }

    println("Part 1 : ${day9part1(input)}")
    println("Part 2 : ${day9part2(input)}")
}