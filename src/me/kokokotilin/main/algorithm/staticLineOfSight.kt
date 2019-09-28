package me.kokokotilin.main.algorithm

import me.kokokotilin.main.geometry.Line
import me.kokokotilin.main.geometry.Vector2f
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.atan2

/**
 * pos: Position des Gegeners
 * edges: Wände, die die Sicht des Gegners einschränken
 * verticies: Anfangs- bzw. Endpunkte der Wände
 *
 * return: eine Liste mit Dreicken, die zusammen das gesamte Sichtfeld ergeben
 */
fun lineOfSight(pos: Vector2f, edges: List<Line>, verticies: List<Vector2f>)
        : MutableList<Triple<Vector2f, Vector2f, Vector2f>> {


    val rays = mutableListOf<Line>()
    val intersections = mutableListOf<Vector2f>()

    // Raycast von der Position des Gegners zu jedem Anfangs- bzw. Endpunkt jeder Wand
    // für jede Vertex werden jeweils 3 Rays gecasted
    // ein Ray direkt zur Vertex und die anderen beiden um einen sehr kleinen Winkel
    // nach links oder rechts rotiert, sodass sie die Vertex nicht direkt treffen
    // -> damit wird sicher gestellt, dass der Algorithmus wirklich alle sichtbaren Flächen berechnet
    // deonn ohne die beiden zusätzlichen Raycasts würden große sichtbare Flächen weggelassen
    for (v in verticies) {
        rays.add(Line(pos, v - pos, false))
        rays.add(Line(pos, v - pos, false) rotate 0.001)
        rays.add(Line(pos, v - pos, false) rotate -0.001)
    }

    // Schnittpunkt jedes Rays mit jeder Wand berechnen
    // hat ein Ray mehrere Schnittpunkte, so wird nur der Schnittpunkt weiterhin betrachtet
    // der den geringsten Abstand zur Position des Gegeners hat
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

    // die Schnittpunkte werden nach ihrem Winkel zur x-Achse berechnet
    // welcher durch die atan2(y, x) Funktion ermittelt wird
    val sortedIntersections = intersections.sortedWith(Comparator { v1, v2 ->
        atan2(v1.y - pos.y, v1.x - pos.x).compareTo(atan2(v2.y - pos.y, v2.x - pos.x))
    } )

    // die Dreicke, die das Sichtfeld darstellen werden zusammengesetzt,
    // in dem immer die Position des Gegners und 2 benachbarte Punkt zu einen Dreieck gemacht werden
    val triangles = mutableListOf<Triple<Vector2f, Vector2f, Vector2f>>()
    for (i in sortedIntersections.indices) {
        triangles.add(Triple(pos, sortedIntersections[i], sortedIntersections[(i + 1) % sortedIntersections.size]))
    }

    return triangles
}