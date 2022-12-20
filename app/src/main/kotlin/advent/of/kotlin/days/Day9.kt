package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines
import kotlin.math.abs

typealias PosXY = Pair<Int, Int>

class Day9 {
    enum class Step {
        LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private fun lineToSteps(s: String): List<Step> {
        val split = s.split(" ")
        val count = split[1]
        return (1..count.toInt())
            .map {
                when (split[0]) {
                    "R" -> Step.RIGHT
                    "L" -> Step.LEFT
                    "U" -> Step.UP
                    "D" -> Step.DOWN
                    else -> throw Exception("god damn")
                }
            }
    }

    private fun parseSteps(lines: List<String>) =
        lines.flatMap { lineToSteps(it) }
    
    fun needsToMove(self: PosXY, head: PosXY): Boolean {
        val xDist = abs(self.first - head.first)
        val yDist = abs(self.second - head.second)
        return xDist > 1 || yDist > 1
    }

    fun move(self: PosXY, head: PosXY): PosXY {
        if (!needsToMove(self, head)) {
            return self
        }

        val xDif = head.first - self.first
        val yDif = head.second - self.second
        
        return when {
            (xDif == 0 && yDif > 0) -> move(self, Step.UP)
            (xDif == 0 && yDif < 0) -> move(self, Step.DOWN)
            (xDif > 0 && yDif == 0) -> move(self, Step.RIGHT)
            (xDif < 0 && yDif == 0) -> move(self, Step.LEFT)
            (xDif > 0 && yDif > 0) -> move(self, Step.UP_RIGHT)
            (xDif < 0 && yDif > 0) -> move(self, Step.UP_LEFT)
            (xDif > 0 && yDif < 0) -> move(self, Step.DOWN_RIGHT)
            (xDif < 0 && yDif < 0) -> move(self, Step.DOWN_LEFT)
            else -> throw Exception("wtf")
        }
    }

    fun move(self: PosXY, step: Step): PosXY {
        return when (step) {
            Step.LEFT -> PosXY(self.first - 1, self.second)
            Step.RIGHT -> PosXY(self.first + 1, self.second)
            Step.UP -> PosXY(self.first, self.second + 1)
            Step.DOWN -> PosXY(self.first, self.second - 1)
            Step.UP_LEFT -> PosXY(self.first - 1, self.second + 1)
            Step.UP_RIGHT -> PosXY(self.first + 1, self.second + 1)
            Step.DOWN_LEFT -> PosXY(self.first - 1, self.second - 1)
            Step.DOWN_RIGHT -> PosXY(self.first + 1, self.second - 1)
        }
    }
    
    fun moveWithTail(steps: List<Step>): Int {
        var head = PosXY(0, 0)
        var tail = PosXY(0, 0)
        val unique = mutableSetOf<PosXY>()
        steps.forEach {
            head = move(head, it)
            tail = move(tail, head)
            unique.add(tail) 
        }
        return unique.size
    }

    fun part1() {
        val lines = readInputFileByLines("day9.txt")
        val steps = parseSteps(lines)
        println(moveWithTail(steps))
    }
}
