package jp.osak.icfpc2020

import java.io.BufferedReader
import java.io.FileReader
import javax.swing.JFrame
import kotlin.streams.asSequence

class Main {
    val parser = GalaxyParser()
    val engine: GalaxyEngine
    init {
        val prelude = loadPrelude()
        engine = GalaxyEngine(prelude)
    }
    fun run() {
        val frame = JFrame("Galaxy")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val canvas = GalaxyCanvas()
        frame.contentPane.add(canvas)
        frame.setSize(600, 600)
        frame.isVisible = true

        canvas.createBufferStrategy(2)
    }

    fun loadPrelude(): Map<String, Term> {
        return BufferedReader(FileReader("galaxy.txt")).use { reader ->
            reader.lines().asSequence().map { line ->
                val values = line.split('=')
                val name = values[0].trim().substring(1)
                Pair(name, parser.parseGalaxy(values[1].trim()))
            }.toMap()
        }
    }

    fun dumpConsList(t: Term) {
        val res = engine.evaluateFull(t)
        if (res is Num) {
            print(res.v)
        } else {
            require (res is Lambda)
            if (res.type == Lambda.Type.NIL) {
                print("nil")
            } else {
                print("(")
                dumpConsList(engine.car(res))
                print(",")
                dumpConsList(engine.cdr(res))
                print(")")
            }
        }
    }

    fun test() {
        val eval = parser.parseGalaxy("ap ap :galaxy nil ap ap cons 0 0")
        val result = engine.evaluateFull(eval)
        dumpConsList(result)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //Main().run()
            Main().test()
        }
    }
}