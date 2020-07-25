package jp.osak.icfpc2020

class GalaxyEngine {
    fun evaluate(t: Term): Term {
        return when (t) {
            is Num -> t
            is App -> evalApp(t)
            is Lambda -> evalLambda(t)
            else -> throw IllegalArgumentException("Unknown term ${t}")
        }
    }

    fun evalApp(t: App): Term {
        val f = evaluateFull(t.f) as Lambda
        require(f.args.size < f.type.arity)
        return Lambda(f.type, f.args + t.arg)
    }

    fun evalLambda(t: Lambda): Term {
        if (t.args.size < t.type.arity) {
            return t
        }
        when (t.type) {
            Lambda.Type.ADD -> {
                val lhs = evaluateFull(t.args[0]) as Num
                val rhs = evaluateFull(t.args[1]) as Num
                return Num(lhs.v + rhs.v)
            }
            Lambda.Type.MUL -> {
                val lhs = evaluateFull(t.args[0]) as Num
                val rhs = evaluateFull(t.args[1]) as Num
                return Num(lhs.v * rhs.v)
            }
            Lambda.Type.DIV -> {
                val lhs = evaluateFull(t.args[0]) as Num
                val rhs = evaluateFull(t.args[1]) as Num
                return Num(lhs.v / rhs.v)
            }
            Lambda.Type.EQ -> {
                val lhs = evaluateFull(t.args[0]) as Num
                val rhs = evaluateFull(t.args[1]) as Num
                if (lhs.v == rhs.v) {
                    return Lambda(Lambda.Type.T)
                } else {
                    return Lambda(Lambda.Type.F)
                }
            }
            Lambda.Type.LT -> {
                val lhs = evaluateFull(t.args[0]) as Num
                val rhs = evaluateFull(t.args[1]) as Num
                if (lhs.v < rhs.v) {
                    return Lambda(Lambda.Type.T)
                } else {
                    return Lambda(Lambda.Type.F)
                }
            }
            Lambda.Type.NEG -> {
                val v = evaluateFull(t.args[0]) as Num
                return Num(-v.v)
            }
            Lambda.Type.S -> return App(App(t.args[0], t.args[2]), App(t.args[1], t.args[2]))
            Lambda.Type.C -> return App(App(t.args[0], t.args[2]), t.args[1])
            Lambda.Type.B -> return App(t.args[0], App(t.args[1], t.args[2]))
            Lambda.Type.T -> return t.args[0]
            Lambda.Type.F -> return t.args[1]
            Lambda.Type.I -> return t.args[0]
            Lambda.Type.CONS -> return App(App(t.args[2], t.args[0]), t.args[1])
            Lambda.Type.CAR -> return App(t.args[0], Lambda(Lambda.Type.T))
            Lambda.Type.CDR -> return App(t.args[0], Lambda(Lambda.Type.F))
            Lambda.Type.NIL -> return Lambda(Lambda.Type.T)
            Lambda.Type.ISNIL -> {
                val arg = evaluateFull(t.args[0])
                return if (arg is Lambda && arg.type == Lambda.Type.T) {
                    Lambda(Lambda.Type.T)
                } else {
                    Lambda(Lambda.Type.F)
                }
            }
        }
    }

    fun evaluateFull(t: Term): Term {
        var cur = t
        while (cur is App || (cur is Lambda && cur.args.size == cur.type.arity)) {
            cur = evaluate(cur)
        }
        return cur
    }
}

interface Term

data class Lambda(val type: Type, val args: List<Term> = listOf()): Term {
    enum class Type (val arity: Int) {
        ADD(2),
        MUL(2),
        DIV(2),
        EQ(2),
        LT(2),
        NEG(1),
        S(3),
        C(3),
        B(3),
        T(2),
        F(2),
        I(1),
        CONS(3),
        CAR(1),
        CDR(1),
        NIL(1),
        ISNIL(1)
    }
}

data class App(val f: Term, val arg: Term) : Term
data class Num(val v: Long) : Term