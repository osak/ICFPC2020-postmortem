package jp.osak.icfpc2020

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileReader
import javax.swing.JFrame
import kotlin.streams.asSequence

class Main {
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
        val parser = GalaxyParser()
        return BufferedReader(FileReader("galaxy.txt")).use { reader ->
            reader.lines().asSequence().map { line ->
                val values = line.split('=')
                val name = values[0].trim().substring(1)
                Pair(name, parser.parseGalaxy(values[1].trim()))
            }.toMap()
        }
    }

    fun test() {
        val prelude = loadPrelude()
        val engine = GalaxyEngine(prelude)
        val parser = GalaxyParser()
        val eval = parser.parseGalaxy("ap ap :galaxy nil ap ap cons 0 0")
        val result = engine.evaluateFull(eval)
        println(result)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //Main().run()
            Main().test()
        }
    }
}