package jp.osak.icfpc2020

import java.lang.IllegalArgumentException
import java.math.BigInteger

class GalaxyModem {
    fun modulate(t: Term, engine: GalaxyEngine): String {
        val cur = engine.evaluateFull(t)
        var result = ""
        if (cur is Num) {
            var value = cur.v
            if (value > 0) {
                result += "01"
            } else {
                result += "10"
                value *= -1
            }
            require (value >= 0)

            if (value == 0L) {
                result += "0"
            } else {
                val bigInt = BigInteger.valueOf(value)
                val bits = (bigInt.bitLength() + 3) / 4 * 4
                result += "1".repeat(bits / 4)
                result += "0"
                result += bigInt.toString(2).padStart(bits, '0')
            }
        } else {
            require (cur is Lambda)
            if (cur.type == Lambda.Type.NIL) {
                result += "00"
            } else {
                require (cur.type == Lambda.Type.CONS)
                result += "11"
                result += modulate(engine.car(cur), engine)
                result += modulate(engine.cdr(cur), engine)
            }
        }
        return result
    }

    fun demodulate(str: String): Term {
        return Context(str).demodulate()
    }

    private class Context(private val str: String) {
        var pos: Int = 0

        fun demodulate(): Term {
            val head = str.substring(pos, pos + 2)
            pos += 2
            return when (head) {
                "00" -> Lambda(Lambda.Type.NIL)
                "01" -> parseNum(1)
                "10" -> parseNum(-1)
                "11" -> parseCons()
                else -> throw IllegalArgumentException(head)
            }
        }

        fun parseNum(sign: Int): Num {
            var bits = 0
            while (str[pos] == '1') {
                bits += 4
                pos++
            }
            pos++

            var value = 0L
            repeat(bits) {
                value *= 2
                if (str[pos] == '1') {
                    value++
                }
                pos++
            }
            return Num(value * sign)
        }

        fun parseCons(): Lambda {
            val car = demodulate()
            val cdr = demodulate()
            return Lambda(Lambda.Type.CONS, listOf(car, cdr))
        }
    }
}

