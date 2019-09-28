package me.kokokotilin.main

import me.kokokotilin.main.geometry.Vector2f
import me.kokokotilin.main.gui.Canvas
import me.kokokotlin.main.drawing.WindowHandler
import me.kokokotlin.main.engine.UpdateHandler
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener

const val WIDTH = 1280
const val HEIGHT = 720

fun main() {
    val windowHandler = WindowHandler(WIDTH, HEIGHT, "LineOfSight")
    val updateHandler = UpdateHandler()

    windowHandler.entities.add(Canvas)
    updateHandler.entities.add(Canvas)

    var shouldMove = false

    windowHandler.addMouseMotionListener(object : MouseMotionListener {
        override fun mouseMoved(e: MouseEvent?) {
            if(e != null && shouldMove) {
                Canvas.pos = Vector2f(e.x.toDouble(), e.y.toDouble())
            }
        }

        override fun mouseDragged(e: MouseEvent?) {
        }
    })

    windowHandler.addKeyListener(object : KeyListener {
        override fun keyTyped(e: KeyEvent?) {
        }

        override fun keyPressed(e: KeyEvent?) {
            shouldMove = e?.keyCode == KeyEvent.VK_CONTROL && !shouldMove
        }

        override fun keyReleased(e: KeyEvent?) {
        }
    })
}