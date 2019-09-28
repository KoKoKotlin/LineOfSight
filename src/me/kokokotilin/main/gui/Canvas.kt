package me.kokokotilin.main.gui

import me.kokokotilin.main.HEIGHT
import me.kokokotilin.main.WIDTH
import me.kokokotilin.main.algorithm.*
import me.kokokotilin.main.geometry.Line
import me.kokokotilin.main.geometry.Vector2f
import me.kokokotilin.main.geometry.sign
import me.kokokotlin.main.drawing.Drawable
import me.kokokotlin.main.engine.Entity
import java.awt.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.random.Random

object Canvas : Drawable, Entity {
    var pos = Vector2f(WIDTH / 2.0 + 100, HEIGHT / 2.0 - 200)

    // Liste mit Wänden
    val edges = listOf(
        // walls
        Line(Vector2f(0.0, 0.0), Vector2f(WIDTH.toDouble(), 0.0), true),
        Line(Vector2f(0.0, 0.0), Vector2f(0.0, HEIGHT.toDouble()), true),
        Line(Vector2f(WIDTH.toDouble(), 0.0), Vector2f(0.0, HEIGHT.toDouble()), true),
        Line(Vector2f(0.0, HEIGHT.toDouble()), Vector2f(WIDTH.toDouble(), 0.0), true),
        // ------
        Line(Vector2f(250.0, 50.0), Vector2f(0.0, 1.0) * 500.0, true),
        Line(Vector2f(250.0, 50.0), Vector2f(-1.0, 0.0) * 210.0, true),
        Line(Vector2f(250.0, 550.0), Vector2f(-1.0, 0.0) * 210.0, true),
        Line(Vector2f(400.0, 50.0), Vector2f(0.0, 1.0) * 300.0, true),
        Line(Vector2f(400.0, 400.0), Vector2f(0.0, 1.0) * 320.0, true),
        Line(Vector2f(400.0, 350.0), Vector2f(1.0, 0.0) * 150.0, true),
        Line(Vector2f(400.0, 400.0), Vector2f(1.0, 0.0) * 200.0, true),
        Line(Vector2f(550.0, 350.0), Vector2f(0.0, -1.0) * 200.0, true),
        Line(Vector2f(600.0, 400.0), Vector2f(0.0, -1.0) * 200.0, true),
        Line(Vector2f(550.0, 150.0), Vector2f(1.0, 0.0) * 250.0, true),
        Line(Vector2f(600.0, 200.0), Vector2f(1.0, 0.0) * 150.0, true),
        Line(Vector2f(800.0, 150.0), Vector2f(0.0, 1.0) * 450.0, true),
        Line(Vector2f(750.0, 200.0), Vector2f(0.0, 1.0) * 350.0, true),
        Line(Vector2f(800.0, 600.0), Vector2f(-1.0, 0.0) * 200.0, true),
        Line(Vector2f(750.0, 550.0), Vector2f(-1.0, 0.0) * 200.0, true),
        Line(Vector2f(600.0, 600.0), Vector2f(0.0, 1.0) * 50.0, true),
        Line(Vector2f(550.0, 550.0), Vector2f(0.0, 1.0) * 150.0, true),
        Line(Vector2f(600.0, 650.0), Vector2f(1.0, 0.0) * 200.0, true),
        Line(Vector2f(550.0, 700.0), Vector2f(1.0, 0.0) * 250.0, true),
        Line(Vector2f(800.0, 0.0), Vector2f(0.0, 1.0) * 150.0, true),
        Line(Vector2f(850.0, 0.0), Vector2f(1.0, 2.5) * 200.0, true),
        Line(Vector2f(1280.0, 720.0), Vector2f(-2.0, -2.5) * 100.0, true),
        Line(Vector2f(1050.0, 0.0), Vector2f(0.0, 1.0) * 150.0, true),
        Line(Vector2f(1050.0, 160.0), Vector2f(0.0, 1.0) * 150.0, true),
        Line(Vector2f(1050.0, 310.0), Vector2f(1.0, 0.0) * 230.0, true)
        )

    val verticies = mutableListOf<Vector2f>()

    // Liste mit den Positionen der Spieler
    val targets = Array(10) { _ ->
        Vector2f(Random.nextDouble(WIDTH.toDouble()), Random.nextDouble(HEIGHT.toDouble()))
    }

    val seenTargets = ConcurrentHashMap<Vector2f, Boolean>()

    init {
        for (e in edges) {
            verticies.add(e.supportVector)
            verticies.add(e.supportVector + e.directionVector)
        }

        for (t in targets) {
            seenTargets[t] = false
        }
    }

    var triangles = lineOfSight(
        pos,
        edges,
        verticies
    )

    override fun draw(g: Graphics) {
        val g2d = g as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val rayBuff = mutableListOf<Line>()
        for (v in verticies) {
            rayBuff.add(Line(pos, v - pos, false))
            rayBuff.add(Line(pos, v - pos, false) rotate 0.001)
            rayBuff.add(Line(pos, v - pos, false) rotate -0.001)
        }

        fun drawPos() {
            g.color = Color.GREEN
            val posSize = 16
            g.fillOval(pos.xR - posSize / 2, pos.yR - posSize / 2, posSize, posSize)
        }

        fun drawEdges() {

            for (e in edges) {
                g.color = Color.RED
                g.stroke = BasicStroke(3f)
                val p1 = e.supportVector
                val p2 = e.supportVector + e.directionVector

                g.drawLine(p1.xR, p1.yR, p2.xR, p2.yR)
            }
        }

        fun drawVertecies() {
            if (SettingsForm.showStatic) {
                for (v in verticies) {
                    g.color = Color.BLACK
                    g.stroke = BasicStroke(1f)

                    val ovalSize = 8
                    g.fillOval(v.xR - ovalSize / 2, v.yR - ovalSize / 2, ovalSize, ovalSize)
                }
            }
        }

        fun drawRays() {
            if (SettingsForm.showRays && SettingsForm.showStatic || SettingsForm.showDynamicRays && !SettingsForm.showStatic) {
                g.color = Color.MAGENTA

                if (SettingsForm.showStatic) {
                    for (r in rayBuff) {
                        r.draw(g)
                    }
                } else {
                    for (r in rays_) {
                        r.draw(g)
                    }
                }
            }
        }

        fun drawIntersections() {
            val allIntersections = mutableListOf<Vector2f>()
            val filteredIntersections = mutableListOf<Vector2f>()

            if (SettingsForm.showStatic) {
                for (r in rayBuff) {
                    var nearest = Vector2f(Double.MAX_VALUE, Double.MAX_VALUE)
                    for (e in edges) {
                        val intersection = r intersect e
                        intersection.ifPresent {
                            allIntersections.add(it)
                            if ((it - pos).mag < (nearest - pos).mag) {
                                nearest = it
                            }
                        }
                    }

                    if (nearest != Vector2f(Double.MAX_VALUE, Double.MAX_VALUE)) filteredIntersections.add(nearest)
                }
            }

            val intersectionBuff = when {
                SettingsForm.showStatic && SettingsForm.showAllIntersections -> allIntersections
                SettingsForm.showStatic && SettingsForm.showFilteredIntersections -> filteredIntersections
                else -> mutableListOf<Vector2f>()
            }

            if (SettingsForm.showStatic) {
                g.color = Color(100, 150, 255, 100)
                intersectionBuff.forEach { v ->
                    g.fillOval(v.xR - 10, v.yR - 10, 20, 20)
                }
            } else {
                if (SettingsForm.showDynamicIntersections) {
                    g.color = Color(100, 150, 255, 100)
                    allIntersections_.forEach { v ->
                        g.fillOval(v.xR - 10, v.yR - 10, 20, 20)
                    }
                }
            }
        }

        fun drawFOV() {
            if (SettingsForm.showStatic) {
                for (t in triangles) {
                    val p1 = t.first
                    val p2 = t.second
                    val p3 = t.third

                    if (SettingsForm.showFOV) {
                        g.color = Color(255, 255, 0, 100)
                        g.fillPolygon(intArrayOf(p1.xR, p2.xR, p3.xR), intArrayOf(p1.yR, p2.yR, p3.yR), 3)
                    }

                    if (SettingsForm.showTriangles) {
                        g.color = Color.BLACK
                        g.drawPolygon(intArrayOf(p1.xR, p2.xR, p3.xR), intArrayOf(p1.yR, p2.yR, p3.yR), 3)
                    }
                }
            } else {
                if (SettingsForm.showDynamicFOV) {
                    g.color = Color(255, 255, 0, 100)
                    g.fillArc(
                        pos.xR - SettingsForm.sightRadius.toInt(),
                        pos.yR - SettingsForm.sightRadius.toInt(),
                        SettingsForm.sightRadius.toInt() * 2, SettingsForm.sightRadius.toInt() * 2,
                        Math.toDegrees(SettingsForm.dir - SettingsForm.fov / 2).toInt(),
                        Math.toDegrees(SettingsForm.fov).toInt()
                    )
                }
            }
        }

        fun drawSightRadius() {
            if (!SettingsForm.showStatic && SettingsForm.showRadius) {
                val dashed = BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0f, floatArrayOf(9f), 0f)
                g2d.stroke = dashed
                g.color = Color(151, 181, 107)
                g.drawOval(
                    pos.xR - SettingsForm.sightRadius.toInt(),
                    pos.yR - SettingsForm.sightRadius.toInt(),
                    SettingsForm.sightRadius.toInt() * 2,
                    SettingsForm.sightRadius.toInt() * 2
                )
            }
        }

        fun drawDir() {
            if (!SettingsForm.showStatic && SettingsForm.showDir) {
                g.color = Color.GREEN.darker()
                g2d.stroke = BasicStroke(3f)
                val scaledDir = pos + SettingsForm.dirVector * (SettingsForm.sightRadius)
                g.drawLine(pos.xR, pos.yR, scaledDir.xR, scaledDir.yR)
            }
        }

        fun drawTargets() {
            for (p in seenTargets) {
                g.color = if (p.value) Color.RED else Color.GRAY

                val v = p.key
                g.fillOval(v.xR - 10, v.yR - 10, 20, 20)
            }
        }

        drawFOV()
        drawEdges()

        drawRays()

        drawIntersections()
        drawVertecies()

        drawSightRadius()
        drawDir()

        drawPos()
        drawTargets()

    }

    override fun update(delta: Double) {
        if (SettingsForm.showStatic) {
            triangles = lineOfSight(
                pos,
                edges,
                verticies
            )
            targets.forEach { t -> seenTargets[t] = triangles.any() { tr -> tr.cointainsPoint(t) } }
        } else {
            rays_.clear()
            allIntersections_.clear()

            targets.forEach {
                seenTargets[it] = lineOfSightWithSideEffects(
                    pos,
                    it,
                    SettingsForm.sightRadius,
                    SettingsForm.dirVector,
                    SettingsForm.fov,
                    edges
                )
            }
        }
    }
}

fun Line.draw(g: Graphics) {
    val p1 = supportVector
    var p2 = supportVector + directionVector

    var i = 0.0
    while (!(p2.x < 0 || p2.x > WIDTH || p2.y < 0 || p2.y > HEIGHT)) {
        p2 = supportVector + directionVector * i
        i += 1
    }

    g.drawLine(p1.xR, p1.yR, p2.xR, p2.yR)
}

fun Triple<Vector2f, Vector2f, Vector2f>.cointainsPoint(v: Vector2f): Boolean {
    val d1 = sign(v, first, second)
    val d2 = sign(v, second, third)
    val d3 = sign(v, third, first)

    val has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0)
    val has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0)

    return !(has_neg && has_pos)
}