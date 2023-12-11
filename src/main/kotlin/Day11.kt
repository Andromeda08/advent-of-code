import kotlin.math.abs

data class Point(val x: Long, val y: Long)
data class Pair(val a: Point, val b: Point, var d: Long)

fun transpose(input: Array<CharArray>): Array<CharArray> {
    val result = Array(input.first().count()) { CharArray(input.count()) }
    (0..<input.count()).forEach { i ->
        (0..<input.first().count()).forEach { j ->
            result[j][i] = input[i][j]
        }
    }
    return result
}

fun getPoints(input: Array<CharArray>): List<Point> {
    val points = mutableListOf<Point>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == '#') { points.add(Point(x.toLong(), y.toLong())) }
        }
    }
    return points
}

fun getPairs(points: List<Point>, distFn: (Point, Point) -> Long): Set<Pair> {
    val pairs = mutableSetOf<Pair>()
    points.forEach { a ->
        points.forEach { b ->
            if (a != b && !pairs.contains(Pair(b, a, distFn(a, b)))) {
                pairs.add(Pair(a, b, distFn(a, b)))
            }
        }
    }
    return pairs
}

fun countEmptyBetween(a: Long, b: Long, input: List<Boolean>): Long {
    if (abs(b - a) <= 1) return 0L
    return ((a + 1)..<b).count { input[it.toInt()] }.toLong()
}

fun manDist(a: Point, b: Point) = abs(b.x - a.x) + abs(b.y - a.y)

fun offsetDistance(a: Point, b: Point, factor: Long, rows: List<Boolean>, cols: List<Boolean>): Long {
    return manDist(offsetPoint(a, factor, rows, cols), offsetPoint(b, factor, rows, cols))
}

fun offsetPoint(a: Point, f: Long, rows: List<Boolean>, cols: List<Boolean>): Point {
    return Point(
        a.x + (countEmptyBetween(0, a.x, cols) * (f - 1)),
        a.y + (countEmptyBetween(0, a.y, rows) * (f - 1)))
}

fun solve(input: Array<CharArray>, factor: Long): Long {
    val emptyRows = input.map { it.all { ch -> ch == '.' } }
    val emptyCols = transpose(input).map { it.all { ch -> ch == '.' } }
    return getPairs(getPoints(input)) { a, b -> offsetDistance(a, b, factor, emptyRows, emptyCols) }.sumOf { it.d }
}

fun main() {
    val input = Input("input_day_11.txt").readLines().map{it.toCharArray()}.toTypedArray()
    println("Part 1 : ${solve(input, 2)}")
    println("Part 2 : ${solve(input, 1000000)}")
}