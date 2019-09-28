package me.kokokotilin.main.geometry

import java.util.*

// Klasse, die eine Strecke oder einen Ray im 2 dimensionalen Raum darstellt
class Line(val supportVector: Vector2f, val directionVector: Vector2f, val segment: Boolean) {

    // berechnet den Schnittpunkt zwischen 2 Lines
    infix fun intersect(line: Line): Optional<Vector2f> {
        val x1 = supportVector.x
        val x2 = directionVector.x
        val y1 = supportVector.y
        val y2 = directionVector.y

        val x3 = line.supportVector.x
        val x4 = line.directionVector.x
        val y3 = line.supportVector.y
        val y4 = line.directionVector.y

        // Lines sind parallel oder gleich
        if (x4 * y2 - x2 * y4 == 0.0) return Optional.empty()

        val t = (y4 * (x1 - x3) + x4 * (y3 - y1)) / (x4 * y2 - x2 * y4)
        val u = (y2 * (x1 - x3) + x2 * (y3 - y1)) / (x4 * y2 - x2 * y4)

        if(t < 0 || u < 0) return Optional.empty()

        val intersection = supportVector + (directionVector * t)

        return when {
            !segment && !line.segment -> Optional.of(intersection)
            !segment && line.segment -> if(u in 0.0..1.0) Optional.of(intersection) else Optional.empty()
            segment && !line.segment -> if(t in 0.0..1.0) Optional.of(intersection) else Optional.empty()
            segment && line.segment -> if(t in 0.0..1.0 && u in 0.0..1.0) Optional.of(intersection) else Optional.empty()
            else -> Optional.empty()
        }
    }

    // Rotiert den directionVector und den supportVector
    infix fun rotate(radians: Double) = Line(supportVector, directionVector rotate radians, segment)
}
