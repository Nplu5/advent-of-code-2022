import GameResult.DRAW
import GameResult.LOOSE
import GameResult.WIN
import Sign.Companion.fromString

fun main() {

    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.split(" ").let { playPairs ->
                Game(fromString(playPairs[0]), fromString(playPairs[1]))
            }
        }.sumOf { strategy ->
            strategy.calculatePlayerOneScore()
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            line.split(" ").let { playPairs ->
                val playTwo = Strategy(playPairs[1]).getSecondPlay(fromString(playPairs[0]))
                Game(fromString(playPairs[0]), playTwo)
            }
        }.sumOf { strategy ->
            strategy.calculatePlayerOneScore()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

sealed class Sign{
    protected abstract val score: Int
    abstract fun scoreAgainst(enemySign: Sign): Int
    abstract fun getDesiredResultSign(gameResult: GameResult): Sign

    companion object{
        fun fromString(code: String): Sign {
            return when(code){
                "X",
                "A" -> Rock
                "Y",
                "B" -> Paper
                "Z",
                "C" -> Scissor
                else -> throw IllegalArgumentException("Unknown code: $code")
            }
        }
    }
    protected val looseScore = 0
    protected val drawScore = 3
    protected val winScore = 6
}

object Rock: Sign(){
    override val score: Int = 1
    override fun scoreAgainst(enemySign: Sign): Int {
        return when(enemySign){
            Rock -> drawScore + score
            Paper -> looseScore + score
            Scissor -> winScore + score
        }
    }

    override fun getDesiredResultSign(gameResult: GameResult): Sign {
        return when(gameResult){
            LOOSE -> Scissor
            DRAW -> Rock
            WIN -> Paper
        }
    }
}
object Paper: Sign(){
    override val score: Int = 2
    override fun scoreAgainst(enemySign: Sign): Int {
        return when(enemySign){
            Rock -> winScore + score
            Paper -> drawScore + score
            Scissor -> looseScore + score
        }
    }

    override fun getDesiredResultSign(gameResult: GameResult): Sign {
        return when(gameResult){
            LOOSE -> Rock
            DRAW -> Paper
            WIN -> Scissor
        }
    }
}
object Scissor: Sign(){
    override val score: Int = 3
    override fun scoreAgainst(enemySign: Sign): Int {
        return when(enemySign){
            Rock -> looseScore + score
            Paper -> winScore + score
            Scissor -> drawScore + score
        }
    }

    override fun getDesiredResultSign(gameResult: GameResult) :Sign {
        return when(gameResult){
            LOOSE -> Paper
            DRAW -> Scissor
            WIN -> Rock
        }
    }
}

class Game(private val playOne: Sign, private val playTwo: Sign){

    fun calculatePlayerOneScore(): Int {
        return playTwo.scoreAgainst(playOne)
    }
}

enum class GameResult{
    LOOSE,
    DRAW,
    WIN;
}

class Strategy(strategy: String){

    private val desiredResult: GameResult

    init {
        desiredResult = when(strategy){
            "X" -> LOOSE
            "Y" -> DRAW
            "Z" -> WIN
            else -> throw IllegalArgumentException("Desired result unknown: $strategy")
        }
    }

    fun getSecondPlay(playOne: Sign): Sign {
        return playOne.getDesiredResultSign(desiredResult)
    }

}