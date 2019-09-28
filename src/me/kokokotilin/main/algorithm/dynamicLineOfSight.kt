package me.kokokotilin.main.algorithm

import me.kokokotilin.main.geometry.Line
import me.kokokotilin.main.geometry.Vector2f
import java.awt.geom.Line2D
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.acos

/**
 *  pos: Position des Gegeners
 *  targetPos: Position des Spielers
 *  sightRadius: Sichtradius des Gegeners
 *  dir: Blickrichtung des Gegeners
 *  fov: Größe des Sichtfeldes des Gegners in Radian -> 0 <= fov <= 2pi
 *  edges: Wände, die die Sicht des Gegners einschränken
 *
 *  return True -> der Gegner kann den Spieler sehen
 *         False -> der Gegner kann den Spieler nicht sehen
 */
fun lineOfSight(pos: Vector2f, targetPos: Vector2f,
                sightRadius: Double, dir: Vector2f, fov: Double, edges: List<Line>) : Boolean {

    // Entfernung vom Gegner zum Spieler berechnen
    val distance = targetPos dist pos
    // Wenn die Entfernung <= dem Sichtradius ist, dann befindet sich der Spieler im Sichtradius des Gegeners
    if(distance <= sightRadius) {
        // Winkel zwischen der Blickrichtung und dem Vektor zwischen Gegener und Spieler berechnen
        val angle = acos((dir * (targetPos - pos)) / (dir.mag * (targetPos - pos).mag))
        // wenn der Winkel x -> 0 <= x <= fov/2 ist, dann befindet der Spieler sich im Sichtfeld
        if(angle in 0.0..(fov / 2)) {
            // Raycast vom Gegner zum Spieler durchführen
            val ray = Line(pos, targetPos - pos, true)
            // wenn sich keine Wände zwischen Gegener und Spieler befinden, dann
            // kann der Gegner den Spieler sehen, ansonsten nicht
            return edges.map { (ray intersect it) }.none() { it.isPresent }
        }
    }

    return false
}

// nur für das Zeichnen im Canvas benötigt
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