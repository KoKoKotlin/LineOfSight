package me.kokokotilin.main.algorithm

import me.kokokotilin.main.geometry.Line
import me.kokokotilin.main.geometry.Vector2f
import java.awt.geom.Line2D
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.acos

fun lineOfSight(pos: Vector2f, targetPos: Vector2f,
                sightRadius: Double, dir: Vector2f, fov: Double, edges: List<Line>) : Boolean {
    val distance = targetPos dist pos
    if(distance <= sightRadius) {
        val angle = acos((dir * (targetPos - pos)) / (dir.mag * (targetPos - pos).mag))
        if(angle in 0.0..(fov / 2)) {
            val ray = Line(pos, targetPos - pos, true)
            return edges.map { (ray intersect it) }.none() { it.isPresent }
        }
    }

    return false
}

var rays_ = CopyOnWriteArrayList<Line>()
var allIntersections_ = CopyOnWriteArrayList<Vector2f>()

fun lineOfSightWithSideEffects(pos: Vector2f, targetPos: Vector2f,
                sightRadius: Double, dir: Vector2f, fov: Double, edges: List<Line>) : Boolean {
    val distance = targetPos dist pos
    if(distance <= sightRadius) {
        val angle = acos((dir * (targetPos - pos)) / (dir.mag * (targetPos - pos).mag))
        if(angle in 0.0..(fov / 2)) {
            val ray = Line(pos, targetPos - pos, true)
            rays_.add(ray)
            edges.map { (ray intersect it).ifPresent {v -> allIntersections_.add(v)} }
            return edges.map { (ray intersect it) }.none() { it.isPresent }
        }
    }


    return false
}