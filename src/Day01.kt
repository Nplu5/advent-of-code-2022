fun main() {
    fun mapToIndexedElves(input: List<String>) = input.fold(mutableListOf(Elf())) { elves, line ->
        when {
            line.isEmpty() -> elves.apply { add(Elf()) }
            else -> elves.apply { last().addCalorie(Calorie(line.toInt())) }
        }
    }.mapIndexed { index, elf ->
        index + 1 to elf
    }

    fun part1(input: List<String>): Int {
        return mapToIndexedElves(input)
            .maxBy { pair -> pair.second.sum }
            .second.sum
    }

    fun part2(input: List<String>): Int {
        return mapToIndexedElves(input)
            .sortedBy { it.second.sum }
            .reversed()
            .slice(0..2)
            .sumOf { it.second.sum }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println(part1(testInput))
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

data class Calorie(val amountOfCalories: Int)

class Elf{
    private val carriedCalories = mutableListOf<Calorie>()

    fun addCalorie(calorie: Calorie){
        carriedCalories.add(calorie)
    }

    val sum: Int
        get() = carriedCalories.sumOf { calorie -> calorie.amountOfCalories }

}