data class Position(var x: Int, var y: Int)
data class Number(val pos: Position, val value: Int, val xRange: IntRange)
data class Symbol(var pos: Position, val char: Char)

fun getInput(): List<String> {
    return Input("input_day_3.txt").readLines()
        .map { ".$it." }
        .also {
            it.addFirst("".padStart(it.count(), '.'))
            it.addLast("".padStart(it.count(), '.'))
        }
}

fun getNumbers(input: List<String>): List<Number> {
    val numbers = mutableListOf<Number>()

    input.forEachIndexed { y, line ->
        var currentNumberPos    = Position(0, y)
        val currentNumberDigits = mutableListOf<Int>()
        var isReadingDigits     = false
        line.forEachIndexed { x, char ->
            val isDigit = char.isDigit()

            val isNewNumber = isDigit && !isReadingDigits
            if (isNewNumber) {
                isReadingDigits = true
                currentNumberPos = Position(x, y)
                currentNumberDigits.clear()
            }

            if (isDigit) {
                currentNumberDigits.add(char.digitToInt())
            }

            val numberFinished = !isDigit && isReadingDigits
            if (numberFinished) {
                isReadingDigits = false
                numbers.add(Number(currentNumberPos, currentNumberDigits.joinToString("").toInt(), IntRange(currentNumberPos.x, currentNumberPos.x + currentNumberDigits.count() - 1)))
            }
        }
    }

    return numbers
}

fun getSymbols(input: List<String>): List<Symbol> {
    val symbols = mutableListOf<Symbol>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, char -> if (isGoodChar(char)) symbols.add(Symbol(Position(x, y), char)) }
    }
    return symbols
}

fun isGoodChar(char: Char): Boolean {
    return char != '.' && !char.isDigit()
}

fun isPartNumber(number: Number, input: List<String>): Boolean {
    val (pos, value) = number
    val digitCount = value.toString().length
    val line = input[pos.y]

    var isPartNumber = false

    if (pos.x > 0) {
        if (isGoodChar(line[pos.x - 1])) isPartNumber = true
    }

    if (pos.x + digitCount < line.length) {
        if (isGoodChar(line[pos.x + digitCount])) isPartNumber = true
    }

    val lowX = (pos.x - 1).coerceAtLeast(0)
    val highX = (pos.x + digitCount).coerceAtMost(line.length)
    for (i in (lowX)..(highX)) {
        if (pos.y - 1 > 0) {
            if (isGoodChar(input[pos.y - 1][i])) isPartNumber = true
        }
        if (pos.y + 1 < line.length) {
            if (isGoodChar(input[pos.y + 1][i])) isPartNumber = true
        }
    }

    return isPartNumber
}

fun day3Part1(input: List<String>): Int {
    return getNumbers(input).filter{isPartNumber(it, input)}.sumOf{it.value}
}

fun day3Part2(input: List<String>): Int {
    val numbers = getNumbers(input).filter{isPartNumber(it, input)}

    return getSymbols(input).sumOf { sy ->
        val regionX = ((sy.pos.x - 1).coerceAtLeast(0))..((sy.pos.x + 1).coerceAtMost(input.first().length))
        val regionY = ((sy.pos.y - 1).coerceAtLeast(0)..((sy.pos.y + 1).coerceAtMost(input.count())))
        val near = mutableSetOf<Number>()

        for (y in regionY) {
            for (x in regionX) {
                numbers.forEach {
                    if (it.pos.y in regionY && x in it.xRange)
                        if (!near.contains(it)) near.add(it)
                }
            }
        }

        if (near.count() == 2) near.map{it.value}.reduce(Int::times) else 0
    }
}

fun main() {
    val input = getInput()

    println("Part 1 : ${day3Part1(input)}")
    println("Part 2 : ${day3Part2(input)}")
}