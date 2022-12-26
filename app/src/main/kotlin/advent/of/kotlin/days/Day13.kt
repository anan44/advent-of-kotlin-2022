package advent.of.kotlin.days

import advent.of.kotlin.utils.readFileAsString

class Day13 {

    enum class Order {
        YES, NO, EVEN
    }

    fun boolToOrder(b: Boolean): Order {
        return if (b) Order.YES else Order.NO
    }

    open class Value
    class Number(val x: Int) : Value() {
        override fun toString(): String {
            return x.toString()
        }

        fun toVec(): Vec {
            return Vec(listOf(this))
        }
    }

    class Vec(val x: List<Value>) : Value() {
        override fun toString(): String {
            return "$x"
        }

        fun head(): Value {
            return x.first()
        }

        fun rest(): Vec {
            return Vec(x.drop(1))
        }
    }

    class Cursor(val s: String) {
        var idx = 1
        private var nestCount = 1
        private val vec = mutableListOf<Value>()
        fun process(): Pair<Vec, Int> {
            when {
                s[idx] == '[' -> {
                    nestCount++
                    val nc = Cursor(s.substring(idx..(s.length - 2)))
                    val helper = nc.process()
                    vec.add(helper.first)
                    idx += helper.second.dec()
                }

                s[idx] == ']' -> {
                    nestCount--
                    if (nestCount == 0) {
                        return Pair(Vec(vec), idx)
                    }
                }

                s[idx].isDigit() && s[idx.inc()].isDigit() -> {
                    val twoDigitNum = "${s[idx].digitToInt()}${s[idx.inc()].digitToInt()}".toInt()
                    vec.add(Number(twoDigitNum))
                    idx++
                }

                s[idx].isDigit() -> {
                    vec.add(Number(s[idx].digitToInt()))
                }
            }
            idx++
            return process()
        }
    }

    fun parsePacketPairs(raw: String): List<Pair<Value, Value>> {
        val twoLines = raw.split("\n\n").map { it.split("\n") }
        return twoLines.map {
            Pair(
                Vec(Cursor(it[0]).process().first.x) as Value,
                Vec(Cursor(it[1]).process().first.x) as Value,
            )
        }
    }

    private fun vecToVecIsInOrder(v1: Vec, v2: Vec): Order {
        return when {
            v1.x.isEmpty() && v2.x.isNotEmpty() -> Order.YES
            v1.x.isNotEmpty() && v2.x.isEmpty() -> Order.NO
            v1.x.isEmpty() && v2.x.isEmpty() -> Order.EVEN
            v1.x.isNotEmpty() && v2.x.isNotEmpty() -> {
                val headPair = Pair(v1.head(), v2.head())
                val headsInOrder = isInOrder(headPair)
                when (headsInOrder) {
                    Order.EVEN -> isInOrder(Pair(v1.rest(), v2.rest()))
                    Order.YES -> Order.YES
                    Order.NO -> Order.NO
                }
            }

            else -> throw Exception("Never")
        }
    }

    private fun isInOrder(pair: Pair<Value, Value>): Order {
        val (v1, v2) = pair
        val result = when {
            v1 is Number && v2 is Number -> {
                if (v1.x == v2.x) Order.EVEN else boolToOrder(v1.x < v2.x)
            }

            v1 is Vec && v2 is Vec -> {
                vecToVecIsInOrder(v1, v2)
            }

            v1 is Vec && v2 is Number -> vecToVecIsInOrder(v1, v2.toVec())
            v1 is Number && v2 is Vec -> vecToVecIsInOrder(v1.toVec(), v2)
            else -> throw Exception("Never")
        }

        return result
    }

    fun part1() {
        val raw = readFileAsString("day13.txt")
        val pairs = parsePacketPairs(raw)

        val result = pairs
            .map { isInOrder(it) }
            .withIndex()
            .toList()
            .filter { it.value == Order.YES || it.value == Order.EVEN }
            .map { it.index.inc() }
            .sum()

        println(result)
    }

    fun compare(v1: Value, v2: Value): Boolean {

        return true
    }

    fun part2() {
        val raw = readFileAsString("day13.txt")
        val pairs = parsePacketPairs(raw)
        
        val divider1 = Vec(Cursor("[[2]]").process().first.x) as Value
        val divider2 = Vec(Cursor("[[6]]").process().first.x) as Value

        val allPackets = pairs
            .flatMap { listOf(it.first, it.second) }
            .toMutableList()
        
        allPackets.add(divider1)
        allPackets.add(divider2)

        val sortedPackets = allPackets.sortedWith { a, b ->
            when (isInOrder(Pair(a, b))) {
                Order.YES -> -1
                Order.NO -> 1
                Order.EVEN -> 0
            }
        }

        val result = sortedPackets
            .withIndex()
            .filter { it.value == divider1 || it.value == divider2 }
            .map  {it.index.inc()}
        
        println(result[0] * result[1])
    }
}