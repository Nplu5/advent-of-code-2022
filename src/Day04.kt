fun main() {

    fun createSections(input: List<String>) = input.flatMap { line -> line.split(",") }
        .map { sectionString -> IntRange.fromSectionString(sectionString) }
        .windowed(2, 2)
        .map { Sections(it) }

    fun part1(input: List<String>): Int {
        return createSections(input)
            .filter { sections -> sections.areFullyOverlapping() }
            .size
    }

    fun part2(input: List<String>): Int {
        return createSections(input)
            .filter { sections -> sections.areOverlapping() }
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    println(part1(testInput))
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))

}


fun IntRange.Companion.fromSectionString(sectionString: String): IntRange{
    return IntRange(
        sectionString.split("-")[0].toInt(),
        sectionString.split("-")[1].toInt()
    )
}

class Sections(sections: List<IntRange>){
    private val firstSection: IntRange = sections[0]
    private val secondSection: IntRange = sections[1]

    init {
        if (sections.size != 2){
            throw IllegalArgumentException("Sections should only contain two separate sections. Contained: $sections")
        }
    }
    
    fun areFullyOverlapping(): Boolean{
        return firstSection.fullyContains(secondSection) || secondSection.fullyContains(firstSection)
    }

    fun areOverlapping(): Boolean {
        return !(firstSection.last < secondSection.first || secondSection.last < firstSection.first)
    }

    private fun IntRange.fullyContains(range: IntRange): Boolean{
        return contains(range.first) && contains(range.last)
    }
}

