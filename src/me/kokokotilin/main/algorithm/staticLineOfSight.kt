package me.kokokotilin.main.algorithm

import me.kokokotilin.main.geometry.Line
import me.kokokotilin.main.geometry.Vector2f
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.atan2

fun lineOfSight(pos: Vector2f, edges: List<Line>, verticies: List<Vector2f>)
        : MutableList<Triple<Vector2f, Vector2f, Vector2f>> {

    // cast rays
    val rays = mutableListOf<Line>()
    val intersections = mutableListOf<Vector2f>()

    for (v in verticies) {
        rays.add(Line(pos, v - pos, false))
        rays.add(Line(pos, v - pos, false) rotate 0.001)
        rays.add(Line(pos, v - pos, false) rotate -0.001)
    }

    for (r in rays) {
        var nearest = Vector2f(Double.MAX_VALUE, Double.MAX_VALUE)
        for (e in edges) {
            val intersection = r intersect e
            intersection.ifPresent {
                if (it dist pos < nearest dist pos) {
                    nearest = it
                }
            }
        }

        if (nearest != Vector2f(Double.MAX_VALUE, Double.MAX_VALUE)) intersections.add(nearest)
    }

    val sortedIntersections = intersections.sortedWith(Comparator { v1, v2 ->
        atan2(v1.y - pos.y, v1.x - pos.x).compareTo(atan2(v2.y - pos.y, v2.x - pos.x))
    } )

    // assemble triangles
    val triangles = mutableListOf<Triple<Vector2f, Vector2f, Vector2f>>()
    for (i in sortedIntersections.indices) {
        triangles.add(Triple(pos, sortedIntersections[i], sortedIntersections[(i + 1) % sortedIntersections.size]))
    }

    return triangles
}