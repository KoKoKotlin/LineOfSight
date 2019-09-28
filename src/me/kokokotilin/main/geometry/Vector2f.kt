package me.kokokotilin.main.geometry

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// Klasse, die einen Vektor im 2 dimensionalen Raum darstellt
class Vector2f(val x: Double, val y: Double) {

    // gerundete Koordinaten
    val xR: Int = x.toInt()
    val yR: Int = y.toInt()

    // Länge des Vektors
    val mag: Double
        get() = sqrt(x * x + y * y)


    operator fun plus(v: Vector2f) = Vector2f(x + v.x, y + v.y)
    operator fun minus(v: Vector2f) = Vector2f(x - v.x, y - v.y)
    operator fun times(scalar: Double) = Vector2f(x * scalar, y * scalar)
    // Skalarprodukt
    operator fun times(v: Vector2f) = x * v.x + y * v.y
    operator fun div(scalar: Double) = Vector2f(x / scalar, y / scalar)

    // Rotiert den Vektor mithilfe der Rotationsmatrix
    infix fun rotate(radians: Double) = Vector2f(x * cos(radians) - y * sin(radians),
        x * sin(radians) + y * cos(radians))

    // berechnet die Entfernund zu einem anderen Vektor
    infix fun dist(v: Vector2f) = (this - v).mag

    override fun toString() = "Vector2f<x: $x y: $y>"
    override fun equals(other: Any?) = other != null && other is Vector2f
            && other.x == x
            && other.y == y

    override fun hashCode(): Int {
        return super.hashCode()
    }

}

fun sign(v1: Vector2f, v2: Vector2f, v3: Vector2f) = (v1.x - v3.x) * (v2.y - v3.y) - (v2.x - v3.x) * (v1.y - v3.y)