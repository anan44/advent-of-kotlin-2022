package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day6 {
    private fun findStartOfPacketIndex(signal: String, len: Int): Int {
        val sizes = (len..signal.length)
            .map { signal.subSequence(it - len, it) }
            .map { it.toSet().size }
       
        return sizes.indexOf(len) + len
    }

    fun part1() {
        val signal = readInputFileByLines("day6.txt").first()
        println(findStartOfPacketIndex(signal, 4))
    }
    
    fun part2() {
        val signal = readInputFileByLines("day6.txt").first()
        println(findStartOfPacketIndex(signal, 14))
    }
}