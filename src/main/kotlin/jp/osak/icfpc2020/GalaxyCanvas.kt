package jp.osak.icfpc2020

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.lang.Double.min
import java.lang.Math.*

data class Vec(val x: Int, val y: Int)

class GalaxyCanvas(private val engine: GalaxyEngine) : Canvas() {
    private var state: Term = Lambda(Lambda.Type.NIL)
    private var data: List<List<Vec>> = listOf()
    private var scale = 5
    private val colors = listOf(Color.BLUE, Color.GREEN, Color.RED, Color.GRAY, Color.ORANGE, Color.PINK)
    private val sender = GalaxySender()

    init {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val px = floor((e.x - width/2).toDouble() / scale).toInt()
                val py = floor((e.y - height/2).toDouble() / scale).toInt()
                println("Click: (${e.x}, ${e.y}), Galaxy: (${px}, ${py}), whs: (${width}, ${height}, ${scale})")
                run(Vec(px, py))
            }
        })
    }

    override fun paint(g: Graphics) {
        require (g is Graphics2D)
        g.setComposite(AlphaComposite.SrcOver.derive(0.5f))
        g.clearRect(0, 0, width, height)
        var i = 0
        for (image in data) {
            g.color = colors[i++]
            for (p in image) {
                g.fillRect(p.x * scale + width / 2, p.y * scale + height / 2, scale, scale)
            }
        }
    }

    fun run(p: Vec) {
        val output = interact(p)
        state = engine.car(output)
        val imagesConsList = engine.car(engine.cdr(output))
        data = imagesConsList.asSequence().map { toVecList(it) }.toList()
        val minX = data.flatMap { img -> img.map { it.x } }.min()!!
        val minY = data.flatMap { img -> img.map { it.y } }.min()!!
        val maxX = data.flatMap { img -> img.map { it.x } }.max()!!
        val maxY = data.flatMap { img -> img.map { it.y } }.max()!!
        scale = min(width.toDouble() / max(abs(minX)+1, abs(maxX)+1), height.toDouble() / max(abs(minY)+1, abs(maxY)+1)).toInt() / 2
        repaint()
    }

    fun interact(p: Vec): Term {
        var currentState = state
        var currentData: Term = Lambda(Lambda.Type.CONS, listOf(Num(p.x.toLong()), Num(p.y.toLong())))
        while (true) {
            val program =
                App(
                    App(
                        Lambda(Lambda.Type.REF, listOf(Name("galaxy"))),
                        currentState
                    ),
                    currentData
                )
            val output = engine.evaluateFull(program)
            val status = engine.car(output) as Num
            if (status.v == 0L) {
                return engine.cdr(output)
            }
            currentState = engine.car(engine.cdr(output))
            val data = engine.car(engine.cdr(engine.cdr(output)))
            currentData = sender.send(data, engine)
        }
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