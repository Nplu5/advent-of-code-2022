import Operation.Companion.fromString
import java.util.*

fun main() {

    fun createInitialConfiguration(input: List<String>): MutableList<Stack<ContentCrate>> {
        val initialBoardGame = input.takeWhile { line -> line.isNotEmpty() }
            .map { configurationString ->
                configurationString.split("")
                    .windowed(4, 4) { it.joinToString("") }
                    .map { field ->
                        when (true) {
                            field.isBlank() -> EmptyCrate
                            else -> ContentCrate(field)
                        }
                    }
            }.reversed() // to start adding crates from the bottom 
            .drop(1)
            .foldIndexed(mutableListOf<Stack<ContentCrate>>()) { index, acc, currentRow ->
                if (index == 0) {
                    currentRow.forEachIndexed { rowIndex, crate ->
                        when (crate) {
                            is EmptyCrate -> acc.add(rowIndex, Stack())
                            is ContentCrate -> acc.add(Stack<ContentCrate>().apply { push(crate) })
                        }
                    }
                } else {
                    currentRow.forEachIndexed { rowIndex, crate ->
                        when (crate) {
                            is ContentCrate -> acc[rowIndex].push(crate)
                        }
                    }
                }
                acc
            }
        return initialBoardGame
    }

    fun part1(input: List<String>): String {
        val initialBoardGame = createInitialConfiguration(input)
        return input.takeLastWhile { line -> line.isNotEmpty() }
            .flatMap { operationString -> fromString(operationString) }
            .fold(Board(initialBoardGame)){ acc, operation ->
                acc.execute(operation)
            }.getSolution()
    }

    fun part2(input: List<String>): String {
        val initialBoard = createInitialConfiguration(input)
        return input.takeLastWhile { line -> line.isNotEmpty() }
            .map { operationString -> Operation9001.fromString(operationString) }
            .fold(Board(initialBoard)){ acc, operation ->
                acc.execute(operation)
            }.getSolution()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    println(part2(testInput))
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))

}


typealias CrateIndex = Int
sealed interface Crate
object EmptyCrate
class ContentCrate(field: String){
    val letter = field.trim()
        .replace("[","")
        .replace("]", "")[0]
        .toString()
}
data class Operation(val source: CrateIndex, val dest: CrateIndex){
    companion object{
        fun fromString(operationString: String): List<Operation>{
            val numberOfRepeats = operationString.split(" ")[1].toInt()
            val source = operationString.split(" ")[3].toInt() - 1
            val destination = operationString.split(" ")[5].toInt() -1 
            return mutableListOf<Operation>().apply {
                repeat(numberOfRepeats){add(Operation(source, destination))}
            }
        }
    }
}

data class  Operation9001(val numberOfRepeats: Int, val source: CrateIndex, val dest: CrateIndex) {
    companion object{
        fun fromString(operationString: String):Operation9001{
            val numberOfRepeats = operationString.split(" ")[1].toInt()
            val source = operationString.split(" ")[3].toInt() - 1
            val destination = operationString.split(" ")[5].toInt() -1
            return Operation9001(numberOfRepeats, source, destination)
        }
    }
}

class Board(private val initialConfiguration: List<Stack<ContentCrate>>) {
    fun execute(operation: Operation) : Board{
        initialConfiguration[operation.source].pop().let { crate ->
            initialConfiguration[operation.dest].push(crate)
        }
        return Board(initialConfiguration)
    }
    
    fun execute(operation: Operation9001) : Board {
        initialConfiguration[operation.source].takeLast(operation.numberOfRepeats).also {  
            repeat(operation.numberOfRepeats){ initialConfiguration[operation.source].pop() }
        }.let { crates ->
            crates.forEach { crate -> initialConfiguration[operation.dest].push(crate)  }
        }
        return Board(initialConfiguration)
    }
    
    fun getSolution(): String{
        return initialConfiguration.joinToString("") { stack -> stack.peek().letter }
    }
}
