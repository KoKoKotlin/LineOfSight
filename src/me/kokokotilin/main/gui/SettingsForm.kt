package me.kokokotilin.main.gui

import me.kokokotilin.main.geometry.Vector2f
import java.awt.*
import java.util.*
import javax.swing.*
import kotlin.math.PI
import kotlin.math.round

object SettingsForm : JFrame("Settings") {
    var showRays = false
    var showAllIntersections = false
    var showFilteredIntersections = false
    var showTriangles = false
    var showFOV = true

    var showStatic = true

    var sightRadius = 250.0
    var fov = PI / 2
    var dir = 0.0
    var showDynamicFOV = false
    var showRadius = false
    var showDir = false
    var showDynamicRays = false
    var showDynamicIntersections = false

    val dirVector: Vector2f
        get() = Vector2f(1.0, 0.0) rotate -dir

    init {
        size = Dimension(600, 350)
        isResizable = false
        setLocation(10, 10)

        val contentPane = JPanel()

        val rShowStatic = JRadioButton("Show Static Method", showStatic)
        val rShowDynamic = JRadioButton("Show Dynamic Method", !showStatic)

        val sRadius = HDoubleJSlider(0.1, 1000.0, sightRadius)
        val sFOV = HDoubleJSlider(0.0, 2 * PI, fov)
        val sDir = HDoubleJSlider(0.0, 2 * PI, dir)

        sRadius.addChangeListener { sightRadius = sRadius.doubleVal }
        sFOV.addChangeListener { fov = sFOV.doubleVal }
        sDir.addChangeListener { dir = sDir.doubleVal }

        val elements1 = arrayOf<Component>(
            JCheckBox("Enable Rays", showRays),
            JCheckBox("Enable All Intersections", showAllIntersections),
            JCheckBox("Enable Filtered Intersections", showFilteredIntersections),
            JCheckBox("Enable Ordering DOESNT WORK", false),
            JCheckBox("Enable Triangles", showTriangles),
            JCheckBox("Enable FOV", showFOV),
            JLabel(""),
            JLabel("")
        )

        val elements2 = arrayOf<Component>(
            sRadius,
            sFOV,
            sDir,
            JCheckBox("Enable Radius", showDynamicRays),
            JCheckBox("Enable FOV", showDynamicFOV),
            JCheckBox("Enable Dir", showDir),
            JCheckBox("Enable Intersections", showDynamicIntersections),
            JCheckBox("Enable Rays", showDynamicRays)
        )


        rShowStatic.addActionListener {
            showStatic = true
            rShowDynamic.isSelected = false
        }

        rShowDynamic.addActionListener {
            showStatic = false
            rShowStatic.isSelected = false
        }

        (elements1[0] as JCheckBox).addActionListener { showRays = (elements1[0] as JCheckBox).isSelected }
        (elements1[1] as JCheckBox).addActionListener {
            showAllIntersections = (elements1[1] as JCheckBox).isSelected
            (elements1[2] as JCheckBox).isSelected = false
            showFilteredIntersections = false
        }
        (elements1[2] as JCheckBox).addActionListener {
            showFilteredIntersections = (elements1[2] as JCheckBox).isSelected
            (elements1[1] as JCheckBox).isSelected = false
            showAllIntersections = false
        }
        (elements1[3] as JCheckBox).addActionListener { println("Doesnt work!") }
        (elements1[4] as JCheckBox).addActionListener { showTriangles = (elements1[4] as JCheckBox).isSelected }
        (elements1[5] as JCheckBox).addActionListener { showFOV = (elements1[5] as JCheckBox).isSelected }

        (elements2[3] as JCheckBox).addActionListener { showRadius = (elements2[3] as JCheckBox).isSelected }
        (elements2[4] as JCheckBox).addActionListener { showDynamicFOV = (elements2[4] as JCheckBox).isSelected }
        (elements2[5] as JCheckBox).addActionListener { showDir = (elements2[5] as JCheckBox).isSelected }
        (elements2[6] as JCheckBox).addActionListener { showDynamicIntersections = (elements2[6] as JCheckBox).isSelected }
        (elements2[7] as JCheckBox).addActionListener { showDynamicRays = (elements2[7] as JCheckBox).isSelected }

        val gLayout = GridLayout(0, 2)
        contentPane.layout = gLayout

        contentPane.add(rShowStatic)
        contentPane.add(rShowDynamic)

        for(r in elements1 zip elements2) {
            contentPane.add(r.first)
            contentPane.add(r.second)
        }



        add(contentPane)
        isVisible = true
    }
}

private class HDoubleJSlider(min: Double, max: Double, init: Double)
    : JSlider(HORIZONTAL, round(min * 1000).toInt(), round(max * 1000).toInt(), round(init * 1000).toInt()) {

    init {
        val labels: Hashtable<Int, JLabel> = Hashtable()
        labels[minimum] = JLabel("$doubleMin")
        labels[maximum] = JLabel("$doubleMax")
        labelTable = labels
        paintLabels = true
    }

    var doubleMin: Double
        get() = minimum / 1000.0
        set(d) {
            minimum = round(d * 1000).toInt()
        }

    var doubleMax: Double
        get() = maximum / 1000.0
        set(d) {
            maximum = round(d * 1000).toInt()
        }

    val doubleVal: Double
        get() = value / 1000.0

}