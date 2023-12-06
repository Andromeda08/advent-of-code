import kotlin.math.*

data class Record(val time: Long, val dist: Long)

fun calculateDistance(holdTime: Double, totalTime: Double): Double {
    return (totalTime - holdTime) * holdTime;
}

fun solveQuadratic(a: Double, b: Double, c: Double): List<Double> {
    val determinant = (b * b) - 4 * a * c
    return if (determinant > 0) {
        val sqrtDeterminant = sqrt(determinant)
        listOf(
            (-b + sqrtDeterminant) / (2 * a),
            (-b - sqrtDeterminant) / (2 * a)
        )
    }
    else if (determinant == 0.0) {
        listOf(-b / (2 * a))
    }
    else {
        throw RuntimeException("Imaginary")
    }
}

fun roundAnswer(ans: Double, time: Double, dist: Double): Double {
    val distFloor = calculateDistance(floor(ans), time)
    return if (distFloor > dist) floor(ans) else ceil(ans)
}

fun calculateHoldTime(dist: Long, totalTime: Long): List<Double> {
    val dDist = dist.toDouble()
    val dTotalTime = totalTime.toDouble()
    return solveQuadratic(-1.0, dTotalTime, -dDist)
        .sorted()
        .map { roundAnswer(it, dTotalTime, dDist) }
}

fun solve(input: List<Record>): Double {
    return input.map { record ->
        val (min, max) = calculateHoldTime(record.dist, record.time)
        println("$min - $max")
        max - min + 1
    }.reduce(Double::times)
}

fun day6Part1(input: List<String>): Double {
    val (t, d) = input.map {
        it.dropWhile { c -> !c.isDigit() }
            .split("\\s+".toRegex())
            .map { i -> i.toLong() }
    }
    val records =  List(t.size) { idx -> Record(t[idx], d[idx]) }
    return solve(records)
}

fun day6Part2(input: List<String>): Double {
    val (t, d) = input.map {
        it.dropWhile { c -> !c.isDigit() }
            .replace("\\s+".toRegex(), "")
            .toLong()
    }
    return solve(listOf(Record(t, d)))
}

fun main() {
    val input = Input("input_day_6.txt").readLines()
    println("Part 1 : ${day6Part1(input)}")
    println("Part 2 : ${day6Part2(input).toBigDecimal().toPlainString()}")
}