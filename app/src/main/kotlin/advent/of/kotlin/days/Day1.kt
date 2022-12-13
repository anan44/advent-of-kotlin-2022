package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines
import java.util.stream.Collectors


class Day1 {
    fun part1() {
        val raw = readInputFileByLines("day1.txt")
        val elves = splitToElves(raw)
        val fattest = elves.maxOfOrNull { it.sum() }
        println(fattest)
    }

    fun part2() {
        val raw = readInputFileByLines("day1.txt")
        val elves = splitToElves(raw)
        val top3 = elves
            .map { it.sum() }
            .sortedDescending()
            .subList(0, 3)
            .sum()
        println(top3)
    }
}

fun splitToElves(raw: List<String>): List<List<Int>> {
    val elves = mutableListOf<MutableList<Int>>()
    var foods = mutableListOf<Int>()
    for (s in raw) {
        if (s == "") {
            elves.add(foods)
            foods = arrayListOf()
            continue
        }
        foods.add(s.toInt())
    }
    return elves
}