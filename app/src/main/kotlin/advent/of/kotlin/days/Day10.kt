package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day10 {
    open class Cycle
    class Noop : Cycle()
    class Add(val x: Int) : Cycle()

    private fun parseCycles(lines: List<String>): List<Cycle> {
        return lines
            .flatMap {
                when (it) {
                    "noop" -> listOf(Noop())
                    else -> {
                        val split = it.split(" ")
                        listOf(Add(split[1].toInt()), Noop())
                    }
                }
            }
    }

    fun runCycle(cycle: Cycle, state: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
        val (register, next, oneAfter) = state
        return when (cycle) {
            is Noop -> Triple(register + next, oneAfter, 0)
            is Add -> Triple(register + next, oneAfter, cycle.x)
            else -> throw Exception("impossible")
        }
    }

    fun runCycles(n: Int, cycles: List<Cycle>): Int {
        var state = Triple(1, 0, 0)
        for (c in cycles.subList(0, n)) {
            state = runCycle(c, state)
        }
        return state.first
    }

    fun signalStrengthForCycle(n: Int, cycles: List<Cycle>) =
        runCycles(n, cycles) * n

    fun part1() {
        val lines = readInputFileByLines("day10.txt")
        val cycles = parseCycles(lines)

        val signalStrengths = listOf(20, 60, 100, 140, 180, 220)
            .sumOf { signalStrengthForCycle(it, cycles) }

        println(signalStrengths)
    }
}