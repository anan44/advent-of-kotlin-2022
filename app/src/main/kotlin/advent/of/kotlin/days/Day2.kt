package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines
import kotlin.Exception

class Day2 {
    private class Round(val me: Hand, val opponent: Hand)
    private class RoundPlan(val opponent: Hand, val goal: Goal)
    private enum class Goal {
        WIN, DRAW, LOSE;

        fun toInt(): Int {
            return when (this) {
                WIN -> 6
                DRAW -> 3
                LOSE -> 0
            }
        }
    }

    private enum class Hand {
        ROCK, PAPER, SCISSORS;

        fun toInt(): Int {
            return when (this) {
                ROCK -> 1
                PAPER -> 2
                SCISSORS -> 3
            }
        }

        fun winsAgainst(): Hand {
            return when (this) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                SCISSORS -> PAPER
            }
        }

        fun drawsAgainst(): Hand {
            return this
        }

        fun losesAgainst(): Hand {
            return when (this) {
                ROCK -> PAPER
                PAPER -> SCISSORS
                SCISSORS -> ROCK
            }
        }
    }

    private fun newHand(s: String): Hand {
        return when (s) {
            "A", "X" -> Hand.ROCK
            "B", "Y" -> Hand.PAPER
            "C", "Z" -> Hand.SCISSORS
            else -> throw Exception()
        }
    }

    private fun newGoal(s: String): Goal {
        return when (s) {
            "X" -> Goal.LOSE
            "Y" -> Goal.DRAW
            "Z" -> Goal.WIN
            else -> throw Exception()
        }
    }

    private fun parseRounds(raw: List<String>): List<Round> {
        return raw.map {
            val split = it.split(" ")
            Round(
                me = newHand(split[1]), opponent = newHand(split[0])
            )
        }
    }

    private fun parseRoundPlans(raw: List<String>): List<RoundPlan> {
        return raw.map {
            val split = it.split(" ")
            RoundPlan(
                opponent = newHand(split[0]),
                goal = newGoal(split[1]),
            )
        }
    }

    private fun isWin(r: Round): Boolean {
        return (r.me == Hand.ROCK && r.opponent == Hand.SCISSORS) ||
                (r.me == Hand.SCISSORS && r.opponent == Hand.PAPER) ||
                (r.me == Hand.PAPER && r.opponent == Hand.ROCK)
    }

    private fun isDraw(r: Round): Boolean {
        return r.me == r.opponent
    }

    private fun calculateRound1(r: Round): Int {
        if (isWin(r)) {
            return 6 + r.me.toInt()
        }

        if (isDraw(r)) {
            return 3 + r.me.toInt()
        }
        return r.me.toInt()
    }

    private fun calculateRound2(r: RoundPlan): Int {
        return when (r.goal) {
            Goal.WIN -> r.opponent.losesAgainst().toInt() + r.goal.toInt()
            Goal.DRAW -> r.opponent.drawsAgainst().toInt() + r.goal.toInt()
            Goal.LOSE -> r.opponent.winsAgainst().toInt() + r.goal.toInt()
        }
    }

    fun part1() {
        val raw = readInputFileByLines("day2.txt")
        val hands = parseRounds(raw)
        val total = hands.sumOf { calculateRound1(it) }
        println(total)
    }

    fun part2() {
        val raw = readInputFileByLines("day2.txt")
        val hands = parseRoundPlans(raw)
        val total = hands.sumOf { calculateRound2(it) }
        println(total)
    }

}
