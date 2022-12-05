fun main() {

    fun part1(input: List<String>): Int {
        return input.map { line -> Rucksack.fromLine(line) }
            .map { rucksack -> rucksack.getDoubleItem() }
            .sumOf { item -> item.mapToPriority() }
    }

    fun part2(input: List<String>): Int {
        return input.map { line -> Rucksack.fromLine(line) }
            .windowed(3, 3){ Group(it) }
            .map { group -> group.getBadge() }
            .sumOf { item -> item.mapToPriority() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    println(part2(testInput))
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))

}

class Group(private val rucksacks: List<Rucksack>){
    fun getBadge(): Item{
        rucksacks.first().items.forEach {item ->
            if (item in rucksacks[1].items && item in rucksacks[2].items){
                return item
            }
        }
        throw IllegalStateException("Group does not have a badge. Rucksacks: ${rucksacks.forEach { println(it) }}")
    }
}

data class Rucksack(val firstCompartment: List<Item>, val secondCompartment: List<Item>){
    val items
        get() = firstCompartment.plus(secondCompartment)

    fun getDoubleItem(): Item {
        firstCompartment.forEach { item ->
            if (item in secondCompartment){
                return item
            }
        }
        throw IllegalStateException("No doubled item in: $firstCompartment and $secondCompartment")
    }

    companion object {
        fun fromLine(line: String): Rucksack{
            val firstCompartment = line.subSequence(0, line.length / 2 ).map { Item(it) }
            val secondCompartment = line.subSequence(line.length / 2, line.length).map { Item(it) }
            return Rucksack(firstCompartment, secondCompartment)
        }
    }
}

@JvmInline
value class Item(val identifier: Char)

private const val SMALL_CHAR_OFFSET = 96
private const val BIG_CHAR_OFFSET = 38
private val priorityDictionary = ('A'..'Z').map { it to it.code - BIG_CHAR_OFFSET}
    .plus(('a'..'z').map { it to it.code - SMALL_CHAR_OFFSET })
    .toMap()

fun Item.mapToPriority(): Int {
    return priorityDictionary[identifier] ?: throw IllegalArgumentException("Identifier $identifier not supported")
}