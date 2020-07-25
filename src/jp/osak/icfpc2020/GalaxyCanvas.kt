package jp.osak.icfpc2020

import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics

data class Vec(val x: Int, val y: Int)

class GalaxyCanvas(private val engine: GalaxyEngine) : Canvas() {
    private var state: Term = Lambda(Lambda.Type.NIL)
    private var data: List<List<Vec>> = listOf()
    private val colors = listOf(Color.BLUE, Color.GREEN, Color.RED, Color.GRAY, Color.ORANGE)

    override fun paint(g: Graphics) {
        g.clearRect(0, 0, width, height)
        var i = 0
        for (image in data) {
            g.color = colors[i++]
            for (p in image) {
                g.fillRect(p.x * 3 + width / 2, p.y * 3 + height / 2, 3, 3)
            }
        }
    }

    fun run(p: Vec) {
        val program =
            App(
                App(
                    Lambda(Lambda.Type.REF, listOf(Name("galaxy"))),
                    state
                ),
                Lambda(Lambda.Type.CONS, listOf(Num(p.x.toLong()), Num(p.y.toLong())))
            )
        val output = engine.evaluateFull(program)
        state = engine.car(engine.cdr(output))
        val imagesConsList = engine.car(engine.cdr(engine.cdr(output)))
        data = imagesConsList.asSequence().map { toVecList(it) }.toList()
        repaint()
    }

    fun toVecList(t: Term): List<Vec> {
        return t.asSequence().map {
            val x = (engine.car(it) as Num).v
            val y = (engine.cdr(it) as Num).v
            Vec(x.toInt(), y.toInt())
        }.toList()
    }

    fun Term.isNil() = this is Lambda && this.type == Lambda.Type.NIL

    fun Term.asSequence(): Sequence<Term> {
        var cur = this
        return sequence {
            while (!cur.isNil()) {
                yield(engine.car(cur))
                cur = engine.cdr(cur)
            }
        }
    }
}