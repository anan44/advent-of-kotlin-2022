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
            return "\n($x)"
        }
    }
    
    class Cursor(val s: String) {
        var idx = 1
        private var nestCount = 1
        private val vec = mutableListOf<Value>()
        fun process(): Vec {
            when {
                s[idx] == '[' -> {
                    nestCount++
                    val nc = Cursor(s.substring(idx..(s.length - 2)))
                    nc.process()
                }
                s[idx] == ']' -> {
                    nestCount--
                    if (nestCount == 0) {
                        println("THING")
                        return Vec(vec)
                    }
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
        // val lines = readInputFileByLines("trial.txt")
        val practice = "[3,[10,5],[8,6]]"
        val cursor = Cursor(practice)
        println(cursor.process())
        
    }
}