package jp.osak.icfpc2020

import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics

data class Vec(val x: Int, val y: Int)

class GalaxyCanvas : Canvas() {
    var data: List<List<Vec>> = listOf()
    val colors = listOf(Color.BLUE, Color.GREEN, Color.RED, Color.GRAY, Color.ORANGE)

    override fun paint(g: Graphics) {
        g.clearRect(0, 0, width, height)
        var i = 0
        for (image in data) {
            g.color = colors[i++]
            for (p in image) {
                g.fillRect(p.x * 3 + width/2, p.y * 3 + height/2, 3, 3)
            }
        }
    }

    fun draw(data: List<List<Vec>>) {
        this.data = data
        repaint()
    }
}