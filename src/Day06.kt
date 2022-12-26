fun main() {
    fun findMarker(input: List<String>, markerSize: Int) =
        input.first().windowed(markerSize, 1) { window ->
            window.toHashSet().size == window.length
        }.mapIndexed { index, value ->
            index to value
        }.first { pair ->
            pair.second
        }.first + markerSize

    fun part1(input: List<String>): Int {
        val markerSize = 4
        return findMarker(input, markerSize)
    }

    fun part2(input: List<String>): Int {
        val markerSize = 14
        return findMarker(input, markerSize)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    println(part2(testInput))
    check(part2(testInput) == 26)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))

}