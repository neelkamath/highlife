package com.neelkamath.highlife

enum class Cell { ALIVE, DEAD }

typealias System = List<MutableList<Cell>>

/** @constructor Creates a [system] of size [rows] and [columns] using a random seed. */
class Highlife(private val rows: Int, private val columns: Int) {
    var system = List(rows) { MutableList(columns) { Cell.values().random() } }
        private set

    init {
        validateSize()
    }

    constructor(seed: System) : this(seed.size, seed[0].size) {
        for (row in seed) {
            if (row.size != system[0].size) throw Exception("Each row must have the same number of columns")
        }
        system = seed
    }

    private fun validateSize() {
        if (rows == 0 || columns == 0) throw Exception("There must be at least one row and column")
    }

    /** Returns whether there is at least one [Cell.ALIVE] in [system]. */
    fun gameIsOver() = system.flatten().count { it == Cell.ALIVE } == 0

    private fun northWestIsAlive(row: Int, column: Int) =
        if (row == 0 || column == 0) false else system[row - 1][column - 1] == Cell.ALIVE

    private fun northIsAlive(row: Int, column: Int) = if (row == 0) false else system[row - 1][column] == Cell.ALIVE

    private fun northEastIsAlive(row: Int, column: Int) =
        if (row == 0 || column == columns - 1) false else system[row - 1][column + 1] == Cell.ALIVE

    private fun westIsAlive(row: Int, column: Int) = if (column == 0) false else system[row][column - 1] == Cell.ALIVE

    private fun eastIsAlive(row: Int, column: Int) =
        if (column == columns - 1) false else system[row][column + 1] == Cell.ALIVE

    private fun southWestIsAlive(row: Int, column: Int) =
        if (row == rows - 1 || column == 0) false else system[row + 1][column - 1] == Cell.ALIVE

    private fun southIsAlive(row: Int, column: Int) =
        if (row == rows - 1) false else system[row + 1][column] == Cell.ALIVE

    private fun southEastIsAlive(row: Int, column: Int) =
        if (row == rows - 1 || column == columns - 1) false else system[row + 1][column + 1] == Cell.ALIVE

    /** Returns the number of neighbors in the Moore neighborhood of the cell at [system]'s [row] and [column]. */
    private fun countNeighbors(row: Int, column: Int) = listOf(
        ::northWestIsAlive, ::northIsAlive, ::northEastIsAlive,
        ::westIsAlive, ::eastIsAlive,
        ::southWestIsAlive, ::southIsAlive, ::southEastIsAlive
    ).filter { it(row, column) }.count()

    /** Updates [system] to its next generation. */
    fun step() {
        val newSystem = List(rows) { row -> MutableList(columns) { column -> system[row][column] } }
        for ((rowIndex, row) in system.withIndex()) {
            for ((columnIndex, column) in row.withIndex()) {
                val neighbors = countNeighbors(rowIndex, columnIndex)
                newSystem[rowIndex][columnIndex] = when {
                    column == Cell.ALIVE && neighbors in 2..3 -> Cell.ALIVE
                    column == Cell.DEAD && neighbors in listOf(3, 6) -> Cell.ALIVE
                    else -> Cell.DEAD
                }
            }
        }
        system = newSystem
    }
}