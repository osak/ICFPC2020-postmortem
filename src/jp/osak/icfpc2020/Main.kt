package jp.osak.icfpc2020

import javax.swing.JFrame

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

    fun test() {
        val engine = GalaxyEngine()
        val parser = GalaxyParser()
        val t = parser.parseGalaxy("ap ap ap s mul ap add 1 6")
        println(t)
        println(engine.evaluateFull(t))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            //Main().run()
            Main().test()
        }
    }
}