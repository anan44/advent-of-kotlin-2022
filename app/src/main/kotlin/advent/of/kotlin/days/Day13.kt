package advent.of.kotlin.days

import advent.of.kotlin.utils.readFileAsString

class Day13 {

    open class Value {
        fun compare(comp: Value): Boolean {
            if (this is Number && comp is Number) {
                return  this.x <= comp.x
            }

            if (this is Vec && comp is Number) {
                val wrapped = Vec(listOf(comp))
                return this.compare(wrapped)
            }

            if (this is Number && comp is Vec) {
                val wrapped = Vec(listOf(this))
                return wrapped.compare(comp)
            }

            if (this is Vec && comp is Vec) {
                return this.compare(comp)
            }
            
            throw Exception("Never")
        }
    }

    class Number(val x: Int) : Value() {
        override fun toString(): String {
            return x.toString()
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

        fun compare(comp: Vec): Boolean {
            if (this.x.isEmpty()) {
                return true
            }
            
            if (comp.x.isEmpty()) {
                return false
            }
            
            val thisHead = this.head()
            val compHead = comp.head()
            if (thisHead is Number && compHead is Number) {
                if (thisHead.x < compHead.x) {
                    return true
                }
                if (thisHead.x > compHead.x) {
                    return false
                }
                
                return thisHead.x == compHead.x && 
                        this.rest().compare(comp.rest())
            }
            
            if (thisHead is Vec && compHead is Number) {
                val wrapped = Vec(listOf(compHead))
                return thisHead.compare(wrapped)
            }
            
            if (thisHead is Number && compHead is Vec) {
                val wrapped = Vec(listOf(thisHead))
                return wrapped.compare(compHead)
            }

            return thisHead.compare(compHead) &&
                    this.rest().compare(comp.rest())
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

    fun parsePacketPairs(raw: String): List<Pair<Vec, Vec>> {
        val twoLines = raw.split("\n\n").map { it.split("\n") }
        return twoLines.map {
            Pair(
                Vec(Cursor(it[0]).process().first.x),
                Vec(Cursor(it[1]).process().first.x),
            )
        }
    }


    fun part1() {
        val raw = readFileAsString("trial.txt")
        val pairs = parsePacketPairs(raw)

        val result = pairs
            .map { it.first.compare(it.second) }
            .withIndex()
            .filter { it.value }
            .map { it.index.inc() }

        println(result)
    }
}