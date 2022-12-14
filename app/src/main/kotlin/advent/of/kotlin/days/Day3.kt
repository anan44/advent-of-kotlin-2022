package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines
import kotlin.streams.toList

class Day3 {
    class Rucksack(val c1: List<Char>, val c2: List<Char>) {
        fun inBoth(): Char {
            val s1 = c1.toSet()
            val s2 = c2.toSet()
            val inter = s1.intersect(s2)
            return inter.first()
        }

    }

    private fun buildPointMap(): Map<Char, Int> {
        val az = ('a'.. 'z').toList()
        val AZ = ('A'..'Z').toList()
        val azAZ = az + AZ
        return azAZ.zip(1..52).toMap()
    }

    private fun parseRucksacks(raw: List<String>): List<Rucksack> {
        return raw
            .map {
                Rucksack(
                    c1 = it.subSequence(0, it.length / 2).toList(),
                    c2 = it.subSequence(it.length / 2, it.length).toList(),
                )
            }
    }

    class Group(val elves: List<List<Char>>) {
        fun findBadge(): Char {
            val e1 = elves[0].toSet()
            val e2 = elves[1].toSet()
            val e3 = elves[2].toSet()

            return e1.intersect(e2).intersect(e3).first()
        }
    }
    private fun parseGroup(sl: List<String>): Group {
        val elves = sl.map { it.toCharArray().toList() }
        return Group(elves)
    }

    private fun parseGroups(raw: List<String>): List<Group> {
        return raw.chunked(3)
            .map { parseGroup(it)}
    }


    fun part1() {
        val raw = readInputFileByLines("day3.txt")
        val groups = parseRucksacks(raw)
        val pointMap = buildPointMap()
        val total = groups
            .map { it.inBoth() }
            .sumOf { pointMap[it]!! }
        println(total)
    }

    fun part2() {
        val raw = readInputFileByLines("day3.txt")
        val groups = parseGroups(raw)
        val pointMap = buildPointMap()
        val total = groups
            .map { it.findBadge() }
            .sumOf { pointMap[it]!! }
        println(total)
    }
}

