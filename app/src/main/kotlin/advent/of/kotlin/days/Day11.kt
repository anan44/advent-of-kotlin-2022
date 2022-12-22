package advent.of.kotlin.days

import advent.of.kotlin.utils.readFileAsString
import com.google.common.math.LongMath.gcd

class Day11Part1 {

    class Jungle(val monkeys: List<Monkey>) {

        fun simulateRounds(n: Int) {
            (0..n.dec()).forEach() { _ ->
                simulateRound()
            }
        }

        private fun simulateRound() {
            for (i in 0..monkeys.size.dec()) {
                simulateMonkey(i)
            }
        }

        private fun simulateMonkey(index: Int) {
            val moves = monkeys[index].prepareMoves()
            monkeys[index].inspectionCount += moves.size
            moves.forEach {
                val target = it.first
                val value = it.second
                monkeys[target].items.add(value)
            }
        }
    }

    class Monkey(
        val items: MutableList<Int>,
        val operation: (Int) -> Int,
        val testDivider: Int,
        val trueTarget: Int,
        val falseTarget: Int,
        var inspectionCount: Int = 0
    ) {
        fun prepareMoves(): List<Pair<Int, Int>> {
            val moves = items.map { calculateMove(it) }
            items.clear()
            return moves
        }

        private fun calculateMove(item: Int): Pair<Int, Int> {
            val value = operation(item) / 3
            val target = if (value % testDivider == 0) trueTarget else falseTarget
            return Pair(target, value)
        }

        override fun toString(): String {
            return items.toString()
        }
    }

    private fun parseMonkeys(raw: String): List<Monkey> {
        val segments = raw.split("\n\n")
        return segments.map { segmentToMonkey(it) }
    }


    private fun segmentToMonkey(segment: String): Monkey {
        val re = """\D+([\d, ]*)\n\D+(\*|\+) (\d+|old)\n\D+(\d+)\n\D+(\d+)\n\D+(\d+)""".toRegex()
        val result = re.find(segment)!!
        val startItems = result.groups[1]!!.value
            .split(",")
            .map { it.trim().toInt() }
            .toMutableList()
        val operator = result.groups[2]!!.value
        val operationX = result.groups[3]!!.value
        val testDivider = result.groups[4]!!.value.toInt()
        val trueTarget = result.groups[5]!!.value.toInt()
        val falseTarget = result.groups[6]!!.value.toInt()
        val operation = when {
            (operationX == "old") -> { x: Int -> x * x }
            (operator == "*") -> { x: Int -> x * operationX.toInt() }
            (operator == "+") -> { x: Int -> x + operationX.toInt() }
            else -> throw Exception("Damn")
        }

        return Monkey(
            items = startItems,
            operation = operation,
            testDivider = testDivider,
            trueTarget = trueTarget,
            falseTarget = falseTarget,
        )
    }

    fun run() {
        val raw = readFileAsString("day11.txt")
        val monkeys = parseMonkeys(raw)
        val jungle = Jungle(monkeys)
        jungle.simulateRounds(20)
        val simulated = jungle.monkeys.sortedByDescending { it.inspectionCount }

        println(simulated[0].inspectionCount * simulated[1].inspectionCount)
    }
}

class Day11Part2 {

    class Jungle(val monkeys: List<Monkey>, val stressReducer: Long) {

        fun simulateRounds(n: Int) {
            (0..n.dec()).forEach() { _ ->
                simulateRound()
            }
        }

        private fun simulateRound() {
            for (i in 0..monkeys.size.dec()) {
                simulateMonkey(i)
            }
        }

        private fun simulateMonkey(index: Int) {
            val moves = monkeys[index].prepareMoves(stressReducer)
            monkeys[index].inspectionCount += moves.size
            moves.forEach {
                val target = it.first
                val value = it.second
                monkeys[target].items.add(value)
            }
        }
    }

    class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val testDivider: Long,
        val trueTarget: Int,
        val falseTarget: Int,
        var inspectionCount: Long = 0
    ) {
        fun prepareMoves(stressReducer: Long): List<Pair<Int, Long>> {
            val moves = items.map { calculateMove(it, stressReducer) }
            items.clear()
            return moves
        }

        private fun calculateMove(item: Long, stressReducer: Long): Pair<Int, Long> {
            val value = operation(item) % stressReducer
            val target = if (value % testDivider == 0.toLong()) trueTarget else falseTarget
            return Pair(target, value)
        }

        override fun toString(): String {
            return items.toString()
        }
    }

    private fun parseMonkeys(raw: String): List<Monkey> {
        val segments = raw.split("\n\n")
        return segments.map { segmentToMonkey(it) }
    }


    private fun segmentToMonkey(segment: String): Monkey {
        val re = """\D+([\d, ]*)\n\D+(\*|\+) (\d+|old)\n\D+(\d+)\n\D+(\d+)\n\D+(\d+)""".toRegex()
        val result = re.find(segment)!!
        val startItems = result.groups[1]!!.value
            .split(",")
            .map { it.trim().toLong() }
            .toMutableList()
        val operator = result.groups[2]!!.value
        val operationX = result.groups[3]!!.value
        val testDivider = result.groups[4]!!.value.toLong()
        val trueTarget = result.groups[5]!!.value.toInt()
        val falseTarget = result.groups[6]!!.value.toInt()
        val operation = when {
            (operationX == "old") -> { x: Long -> x * x }
            (operator == "*") -> { x: Long -> x * operationX.toLong() }
            (operator == "+") -> { x: Long -> x + operationX.toLong() }
            else -> throw Exception("Damn")
        }

        return Monkey(
            items = startItems,
            operation = operation,
            testDivider = testDivider,
            trueTarget = trueTarget,
            falseTarget = falseTarget,
        )
    }
    
    private fun lcm(a: Long, b: Long) = a * b / gcd(a,b)

    fun run() {
        val raw = readFileAsString("day11.txt")
        val monkeys = parseMonkeys(raw)
        val stressReducer = monkeys
            .map { it.testDivider }
            .reduce { acc, x -> lcm(acc, x) }
        val jungle = Jungle(monkeys, stressReducer)
        
        jungle.simulateRounds(10_000)
        val simulated = jungle.monkeys
            .map { it.inspectionCount }
            .sortedDescending()
        
        println(simulated[0] * simulated[1])
    }
}