package jp.osak.icfpc2020

import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStreamReader
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

        val canvas = GalaxyCanvas(engine)
        frame.contentPane.add(canvas)
        frame.setSize(800, 800)
        frame.isVisible = true

        canvas.createBufferStrategy(2)

        canvas.run(Vec(0, 0))
    }

    fun loadPrelude(): Map<String, Term> {
        val isr = InputStreamReader(this::class.java.getResourceAsStream("/galaxy.txt"))
        return BufferedReader(isr).use { reader ->
            reader.lines().asSequence().map { line ->
                val values = line.split('=')
                val name = values[0].trim().substring(1)
                Pair(name, parser.parseGalaxy(values[1].trim()))
            }.toMap()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
            //Main().test()
        }
    }
}