package jp.osak.icfpc2020

import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics

class GalaxyCanvas : Canvas() {
    override fun paint(g: Graphics) {
        g.clearRect(0, 0, width, height)
        g.color = Color.RED
        g.drawLine(10, 10, 400, 400)
    }
}