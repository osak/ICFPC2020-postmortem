package jp.osak.icfpc2020

class GalaxyParser {
    fun parseGalaxy(code: String): Term {
        return Context(code).parseTerm()
    }
}

private class Context(val code: String) {
    private var pos: Int = 0

    fun parseTerm(): Term {
        while (code[pos] == ' ') {
            pos++
        }
        when {
            code.startsWith("ap ", pos) -> {
                pos += 3
                val f = parseTerm()
                val v = parseTerm()
                return App(f, v)
            }
            code[pos].isDigit() -> {
                val start = pos
                while (pos < code.length && code[pos].isDigit()) {
                    pos++
                }
                return Num(code.substring(start, pos).toLong())
            }
            else -> {
                val start = pos
                while (pos < code.length && code[pos] != ' ') {
                    pos++
                }
                return Lambda(Lambda.Type.valueOf(code.substring(start, pos).toUpperCase()))
            }
        }
    }
}