package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day6 {
    private fun findStartOfPacketIndex(signal: String): Int {
        val sizes = (4..signal.length)
            .map { signal.subSequence(it - 4, it) }
            .map { it.toSet().size }
       
        return sizes.indexOf(4) + 4
    }

    fun part1() {
        val signal = readInputFileByLines("day6.txt").first()
        println(findStartOfPacketIndex(signal))
    }
}