package com.neelkamath.highlife

import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.XYSeries
import org.knowm.xchart.style.Styler
import org.knowm.xchart.style.markers.SeriesMarkers
import java.awt.Color
import javax.swing.SwingUtilities

/** A Cartesian coordinate for a [cell] where [x] is the abscissa, and [y] is the ordinate. */
private data class CellCoordinate(val cell: Cell, val x: Int, val y: Int)

/**
 * Returns a human readable [String] from [system].
 *
 * Each row will be separated by a new line, and each column a space. [Cell.ALIVE] and [Cell.DEAD] are represented by
 * `1` and `0` respectively.
 */
private fun prettifySystem(system: System) =
    system.joinToString("\n") { row -> row.joinToString(" ") { if (it == Cell.ALIVE) "1" else "0" } }

internal fun playOnStdout(highlife: Highlife, steps: Int) = with(highlife) {
    println("Seed:\n${prettifySystem(system)}")
    var generation = 0
    while (true) {
        if (gameIsOver() || (steps != -1 && generation == steps)) break
        step()
        println("Generation ${++generation}\n${prettifySystem(system)}")
    }
    if (gameIsOver()) println("The game is over.")
}

internal class GuiPlayer(private val highlife: Highlife, steps: Int, delay: Long) {
    private val chart = XYChartBuilder().theme(Styler.ChartTheme.GGPlot2).build().also {
        with(it.styler) {
            chartBackgroundColor = Color.GRAY
            legendBackgroundColor = Color.GRAY
            chartFontColor = Color.BLACK
            isPlotGridLinesVisible = false
            isAxisTicksMarksVisible = false
            isXAxisTicksVisible = false
            isYAxisTicksVisible = false
            plotBackgroundColor = Color.BLACK
            defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Scatter
            seriesColors = arrayOf(Color.WHITE, Color.BLACK)
            seriesMarkers = arrayOf(SeriesMarkers.SQUARE)
        }
    }

    init {
        updateChart("Seed")
        val gui = SwingWrapper(chart)
        gui.displayChart()
        var generation = 0
        while (true) {
            if (steps != -1 && generation == steps) break
            if (highlife.gameIsOver()) {
                displayGameOver(generation, gui)
                break
            }
            highlife.step()
            Thread.sleep(delay)
            SwingUtilities.invokeLater {
                updateChart("Generation ${++generation}")
                gui.repaintChart()
            }
        }
    }

    /**
     * Returns [Highlife.system]'s [Cell]s as a [List] of [CellCoordinate]s.
     *
     * For example, if the system is the one shown below (where `1` and `0` denote [Cell.ALIVE] and [Cell.DEAD]
     * respectively),
     * ```
     * 1 0
     * 0 1
     * ```
     * the Cartesian coordinates would be (1, 2), (2, 2), (1, 1), and (2, 1) respectively.
     */
    private fun getCellCoordinates() = highlife.system.mapIndexed { x, row ->
        row.mapIndexed { y, cell -> CellCoordinate(cell, y + 1, highlife.system.size - x) }
    }.flatten()

    private fun updateSeries(coordinates: List<CellCoordinate>, state: Cell) {
        chart.updateXYSeries(
            state.toString(),
            getCoordinates(coordinates, state, isX = true), getCoordinates(coordinates, state, isX = false),
            null
        )
    }

    private fun addSeries(coordinates: List<CellCoordinate>, state: Cell) {
        chart.addSeries(
            state.toString(),
            getCoordinates(coordinates, state, isX = true), getCoordinates(coordinates, state, isX = false),
            null
        )
    }

    private fun updateCoordinates(coordinates: List<CellCoordinate>, state: Cell) =
        if (chart.seriesMap.containsKey(state.toString())) {
            updateSeries(coordinates, state)
        } else {
            addSeries(coordinates, state)
        }

    private fun updateChart(title: String) {
        chart.title = title
        val coordinates = getCellCoordinates()
        updateCoordinates(coordinates, Cell.ALIVE)
        updateCoordinates(coordinates, Cell.DEAD)
    }

    private fun getCoordinates(coordinates: List<CellCoordinate>, state: Cell, isX: Boolean) =
        coordinates.filter { it.cell == state }.map { if (isX) it.x else it.y }.map { it.toDouble() }.toDoubleArray()

    private fun displayGameOver(generation: Int, gui: SwingWrapper<XYChart>) = SwingUtilities.invokeLater {
        chart.title = "Game ended after $generation generations"
        gui.repaintChart()
    }
}