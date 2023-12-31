class Mapping(dstStart: Long, srcStart: Long, private val range: Long) {
    private val srcRange = LongRange(srcStart, (srcStart + range - 1).coerceAtLeast(0))
    private val dstRange = LongRange(dstStart, (dstStart + range - 1).coerceAtLeast(0))

    fun srcInMapping(src: Long): Boolean {
        return srcRange.contains(src)
    }
    fun mapSrcToDst(src: Long): Long {
        return if (src !in srcRange) src else dstRange.elementAt(srcRange.indexOf(src))
    }

    override fun toString(): String {
        return "($range) Src: $srcRange | Dst: $dstRange"
    }
}

data class Database(
    val seedToSoil: List<Mapping>,
    val soilToFert: List<Mapping>,
    val fertToWater: List<Mapping>,
    val waterToLight: List<Mapping>,
    val lightToTemp: List<Mapping>,
    val tempToHum: List<Mapping>,
    val humToLoc: List<Mapping>,
)

fun getSrcToDst(maps: List<Mapping>, src: Long): Long {
    maps.forEach { if (it.srcInMapping(src)) return it.mapSrcToDst(src) }
    return src
}

fun getInputSection(section: String, input: List<String>): List<Mapping> {
    return input
        .dropWhile { it != "$section map:" }
        .takeWhile { it.isNotEmpty() }
        .drop(1)
        .map {
            val (a, b, c) = it.split(" ").map { i -> i.trim().toLong() }
            Mapping(a, b, c)
        }
}

fun findMin(seeds: List<Long>, db: Database): Long {
    return seeds.map { seed ->
        val soil = getSrcToDst(db.seedToSoil, seed)
        val fert = getSrcToDst(db.soilToFert, soil)
        val water = getSrcToDst(db.fertToWater, fert)
        val light = getSrcToDst(db.waterToLight, water)
        val temp = getSrcToDst(db.lightToTemp, light)
        val hum = getSrcToDst(db.tempToHum, temp)
        getSrcToDst(db.humToLoc, hum)
    }.toList().minOf{it}
}

fun day5Part1(db: Database, input: List<String>): Long {
    val seeds = input.first().drop(7).split(" ").map { it.toLong() }
    return findMin(seeds, db)
}

fun walkMaps(id: Long, idx: Int, maps: List<List<Mapping>>): Long {
    if (idx == maps.count()) {
        return id
    }
    return walkMaps(getSrcToDst(maps[idx], id), idx + 1, maps)
}

fun day5part1Recursive(db: Database, input: List<String>): Long {
    val seeds = input.first().drop(7).split(" ").map { it.toLong() }
    val maps = listOf(db.seedToSoil, db.soilToFert, db.fertToWater, db.waterToLight, db.lightToTemp, db.tempToHum, db.humToLoc)
    return seeds.map { walkMaps(it, 0, maps) }.toList().min()
}

fun day5part2Recursive(db: Database, input: List<String>): Long {
    val seeds = input.first().drop(7).split(" ").map { it.toLong() }
        .windowed(2, 2)
        .map { LongRange(it[0], it[0] + it[1] - 1) }
        .flatten()
        .sorted()

    val maps = listOf(db.seedToSoil, db.soilToFert, db.fertToWater, db.waterToLight, db.lightToTemp, db.tempToHum, db.humToLoc)

    return seeds.map { walkMaps(it, 0, maps) }.toList().min()
}

fun main() {
    val input = Input("input_day_5_test.txt").readLines()
    val db = Database(
        getInputSection("seed-to-soil", input),
        getInputSection("soil-to-fertilizer", input),
        getInputSection("fertilizer-to-water", input),
        getInputSection("water-to-light", input),
        getInputSection("light-to-temperature", input),
        getInputSection("temperature-to-humidity", input),
        getInputSection("humidity-to-location", input)
    )

    println("Part 1 : ${day5part1Recursive(db, input)}")
    println("Part 2 : ${day5part2Recursive(db, input)}")
}