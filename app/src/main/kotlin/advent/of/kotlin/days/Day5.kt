package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day5 {
    private class Stacks(val stacks: MutableList<Stack>) {
        override fun toString(): String {
            return stacks.joinToString("\n")
        }

        fun makeMoves(moves: List<Move>) {
            for (m in moves) {
                val container = stacks[m.from - 1].containers.removeLast()
                stacks[m.to - 1].containers.add(container)

            }
        }

        fun makeBigMoves(moves: List<Move>) {
            for (m in moves) {
                val moving = stacks[m.from - 1].containers.takeLast(m.count)
                stacks[m.from - 1].containers = stacks[m.from - 1].containers.dropLast(m.count).toMutableList()
                stacks[m.to -1 ].containers.addAll(moving)
            }
        }

        fun result(): String {
            return stacks
                .map { it.containers.last() }
                .joinToString("")
        }

    }

    private fun linesToStacks(lines: List<String>): Stacks {
        val towerCount = lines
            .last()
            .trim()
            .split(" ")
            .last()
            .toInt()

        val charLines = lines.map { it.toList() }

        val stacks = (1..towerCount.toInt())
            .map { columnsToStack(it, charLines) }
            .toMutableList()

        return Stacks(stacks)
    }

    private fun columnsToStack(colIndex: Int, columns: List<List<Char>>): Stack {
        val numChar = "$colIndex"
            .toList()
            .first()
        val index = columns
            .last()
            .indexOf(numChar)

        val containers = columns
            .map { it[index] }
            .reversed()
            .takeWhile { it != ' ' }
        return Stack(containers.subList(1, containers.size).toMutableList())
    }

    private class Stack(var containers: MutableList<Char>) {
        override fun toString(): String {
            return containers
                .map { it.toString() }
                .joinToString(" ")
        }
    }

    private class Move(val count: Int, val from: Int, val to: Int) {
        override fun toString(): String {
            return "$count $from $to"
        }
    }

    private fun parseMoves(lines: List<String>): List<Move> {
        return lines
            .map {
                val x = it.split(" ")
                Move(
                    count = x[1].toInt()!!,
                    from = x[3].toInt()!!,
                    to = x[5].toInt()!!
                )
            }
    }

    private fun flatMoves(moves: List<Move>): List<Move> {
        return moves
            .flatMap {
                val move = it
                val len = move.count - 1
                (0..len).map { Move(1, move.from, move.to) }
            }
    }


    fun part1() {
        val lines = readInputFileByLines("day5.txt")
        val stackInput = lines.takeWhile { it.isNotEmpty() }
        val stacks = linesToStacks(stackInput)
        val moveInput = lines.subList(stackInput.size + 1, lines.size)
        val moves = flatMoves(parseMoves(moveInput))
        
        stacks.makeMoves(moves)
        println(stacks.result())
    }

    fun part2() {
        val lines = readInputFileByLines("day5.txt")
        val stackInput = lines.takeWhile { it.isNotEmpty() }
        val stacks = linesToStacks(stackInput)
        val moveInput = lines.subList(stackInput.size + 1, lines.size)
        val moves = parseMoves(moveInput)

        stacks.makeBigMoves(moves)
        println(stacks.result())
        
    }
}