package advent.of.kotlin.days

import advent.of.kotlin.utils.readInputFileByLines

class Day13 {

    open class Value

    class Number(val x: Int) : Value() {
        override fun toString(): String {
            return x.toString()
        }
    }

    class Vec(val x: List<Value>) : Value() {
        override fun toString(): String {
            return "$x"
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

    fun part1() {
        val lines = readInputFileByLines("trial.txt")
        val practice = "[3,[10,5],[8,6]]"
        val list = Cursor(practice).process().first
        println(list.x)
        
    }
}