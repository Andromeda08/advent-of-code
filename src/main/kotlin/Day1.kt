fun day1Part1(input: List<String>): Int {
    var sum = 0

    input.forEach { line ->
        val digits = line.filter { it.isDigit() }.map { it.digitToInt() }
        sum += digits.first() * 10 + digits.last()
    }

    return sum
}

fun day1Part2(input: List<String>): Int {
    var sum = 0
    val numbers = mapOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9)

    input.forEach { line ->
        val digits = mutableListOf<Int>()
        line.forEachIndexed { idx, it ->
            var digit = 0
            if (it.isDigit()) {
                digit = it.digitToInt()
            }
            else {
                val substr = line.drop(idx)
                numbers.forEach { n -> if (n.key in substr.take(n.key.length)) digit = n.value }
            }
            if (digit != 0)
            {
                digits.add(digit)
            }
        }
        sum += digits.first() * 10 + digits.last()
    }

    return sum
}

fun main()
{
    val input = Input("input_day_1.txt").readLines()

    val part1 = day1Part1(input)
    println("Part 1 : $part1")

    val part2 = day1Part2(input)
    println("Part 2 : $part2")
}