package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

typealias XY = Pair<Int, Int>

const val startHeight = 0
const val endHeight = 27

class Day12 {
    data class Path(val head: XY, val tail: Set<XY>)

    class Mountain(private val heights: List<List<Int>>) {
        private fun getHeight(pos: XY): Int {
            return heights[pos.second][pos.first]
        }

        fun possibleMoves(self: XY): List<XY> {
            val selfHeight = getHeight(self)
            val directions = listOf(
                XY(self.first, self.second.dec()), // Up
                XY(self.first, self.second.inc()), // Down
                XY(self.first.dec(), self.second), // Left
                XY(self.first.inc(), self.second), // Right
            )

            return directions.mapNotNull { moveOrNil(selfHeight, it) }
        }

        private fun moveOrNil(selfHeight: Int, target: XY): XY? {
            if (!isInside(target)) {
                return null
            }
            if (selfHeight + 1 >= getHeight(target)) {
                return target
            }
            return null
        }

        private fun isInside(target: XY): Boolean {
            return when {
                target.first < 0 -> false
                target.second < 0 -> false
                target.first >= heights.first().size -> false
                target.second >= heights.size -> false
                else -> true
            }
        }

        fun findStart(): XY {
            for (y in 0..heights.size.dec()) {
                for (x in 0..heights.first().size.dec()) {
                    if (getHeight(XY(x, y)) == 0) {
                        return XY(x, y)
                    }
                }
            }
            throw Exception("No start")
        }

        fun process(start: XY): Int {
            val visited = mutableSetOf<XY>()
            var paths = listOf(Path(start, setOf()))
            while (true) {
                val temp = mutableListOf<Path>()
                for (p in paths) {
                    val newMoves = possibleMoves(p.head)
                        .filter { it !in visited }
                        .map { Path(it, p.tail + p.head) }


                    newMoves.forEach { visited.add(it.head) }
                    temp.addAll(newMoves)
                }

                val end = paths.find { getHeight(it.head) == endHeight }
                if (end != null) {
                    return end.tail.size
                }

                if (paths.isEmpty()) {
                    return -1
                }

                paths = temp
            }
        }

        fun findWithHeight(height: Int): List<XY> {
            val positions = mutableListOf<XY>()
            for (y in 0..heights.size.dec()) {
                for (x in 0..heights.first().size.dec()) {
                    if (getHeight(XY(x, y)) == height) {
                        positions.add(XY(x, y))
                    }
                }
            }
            return positions
        }
    }

    private fun parseMountain(lines: List<String>): Mountain {
        val heights = lines
            .map { line ->
                line.toCharArray()
                    .map { it.code }
                    .map { it - 96 }
                    .map { if (it == -13) startHeight else it }
                    .map { if (it == -27) endHeight else it }
            }
        return Mountain(heights)
    }

    fun part1() {
        val lines = readInputFileByLines("day12.txt")
        val mountain = parseMountain(lines)
        val start = mountain.findStart()
        mountain.process(start)
    }

    fun part2() {
        val lines = readInputFileByLines("day12.txt")
        val mountain = parseMountain(lines)
        val starts = mountain.findWithHeight(1)
        val distances = starts.map { mountain.process(it) }
        val shortestDistance = distances
            .filter { it > 0 }
            .minOf { it }
        println(shortestDistance)
    }
}