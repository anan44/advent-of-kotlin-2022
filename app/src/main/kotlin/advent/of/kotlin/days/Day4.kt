package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day4 {
    class ElfPair(s: String) {
        private val sections: Pair<Set<Int>, Set<Int>>

        init {
            val split = s.split(",")
            val elf1 = split[0].split("-")
            val elf2 = split[1].split("-")
            sections = Pair(
                (elf1[0].toInt()..elf1[1].toInt()).toSet(),
                (elf2[0].toInt()..elf2[1].toInt()).toSet()
            )
        }

        override fun toString(): String {
            return "${sections.first} - ${sections.second}"
        }

        fun eitherOneFullyContains(): Boolean {
            return sections.first.containsAll(sections.second) ||
                    sections.second.containsAll(sections.first)
        }

        fun overlapsAtAll(): Boolean {
            return sections.first
                .intersect(sections.second)
                .isEmpty()
                .not()
        }
    }

    private fun parsePairs(raw: List<String>): List<ElfPair> {
        return raw
            .map { ElfPair(it) }
    }

    fun part1() {
        val raw = readInputFileByLines("day4.txt")
        val pairs = parsePairs(raw)
        val total = pairs.count { it.eitherOneFullyContains() }
        println(total)
    }

    fun part2() {
        val raw = readInputFileByLines("day4.txt")
        val pairs = parsePairs(raw)
        val total = pairs.count { it.overlapsAtAll() }
        println(total)

    }
}