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

    private fun runCycle(cycle: Cycle, state: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
        val (register, next, oneAfter) = state
        return when (cycle) {
            is Noop -> Triple(register + next, oneAfter, 0)
            is Add -> Triple(register + next, oneAfter, cycle.x)
            else -> throw Exception("impossible")
        }
    }

    private fun runCycles(n: Int, cycles: List<Cycle>): Int {
        var state = Triple(1, 0, 0)
        for (c in cycles.subList(0, n)) {
            state = runCycle(c, state)
        }
        return state.first
    }

    private fun signalStrengthForCycle(n: Int, cycles: List<Cycle>) =
        runCycles(n, cycles) * n

    private fun isOnSprite(index: Int, sprite: Triple<Int, Int, Int>): Boolean {
        val fixedIndex = index % 40
        return fixedIndex >= sprite.first && fixedIndex <= sprite.first + 2
    }

    private fun calculatePixels(cycles: List<Cycle>): List<String> {
        var sprite = Triple(0, 0, 0)
        val pixels = mutableListOf<String>()
        for (i in 0..cycles.size.dec()) {
            sprite = runCycle(cycles[i], sprite)
            val pixel = if (isOnSprite(i, sprite)) "#" else " "
            pixels.add(pixel)
        }
        return pixels
    }

    private fun draw(pixels: List<String>) {
        val s = pixels.joinToString("")
        val lines = s.chunked(40)
        lines.forEach { println(it) }
    }

    fun part1() {
        val lines = readInputFileByLines("day10.txt")
        val cycles = parseCycles(lines)

        val signalStrengths = listOf(20, 60, 100, 140, 180, 220)
            .sumOf { signalStrengthForCycle(it, cycles) }

        println(signalStrengths)
    }

    fun part2() {
        val lines = readInputFileByLines("day10.txt")
        val cycles = parseCycles(lines)
        val pixels = calculatePixels(cycles)
        draw(pixels)
    }
}