package com.neelkamath.highlife

private enum class SystemOption { RANDOM, SUPPLIED }

/**
 * Returns a [System] from [string] where each row has size [columns].
 *
 * `A`s and `D`s in the string represent [Cell.ALIVE] and [Cell.DEAD] respectively. For example, if [columns] is `4`,
 * and [string] is `AADDDDDA`, the [System] will have two rows and four columns.
 */
private fun createSystem(string: String, columns: Int) =
    string.map { if (it == 'A') Cell.ALIVE else Cell.DEAD }.chunked(columns).map { it.toMutableList() }

/**
 * Returns a human readable [String] from [system].
 *
 * Each row will be separated by a new line, and each column a space. [Cell.ALIVE] and [Cell.DEAD] are represented by
 * `1` and `0` respectively.
 */
private fun prettifySystem(system: System) =
    system.joinToString("\n") { row -> row.joinToString(" ") { if (it == Cell.ALIVE) "1" else "0" } }

private fun read(things: String, min: Int): Int {
    print("Enter the number of $things: ")
    val input = readLine()!!.toIntOrNull() ?: read(things, min)
    return if (input < min) read(things, min) else input
}

private fun readOption(): SystemOption =
    print("Enter \"r\" to use a randomly generated seed, or \"s\" to supply your own: ").run {
        when (readLine()!!) {
            "r" -> SystemOption.RANDOM
            "s" -> SystemOption.SUPPLIED
            else -> readOption()
        }
    }

private fun isValidSystem(system: String, columns: Int) =
    system.all { it in listOf('A', 'D') } && system.length >= columns && system.length % columns == 0

private fun readSystem(columns: Int): String {
    print("Enter the seed using \"A\" and \"D\" to represent alive and dead cells respectively ")
    print("(e.g., \"ADDDAA\" has two rows, and three columns): ")
    val input = readLine()!!
    return if (isValidSystem(input, columns)) input else readSystem(columns)
}

private fun playForever(highlife: Highlife) = with(highlife) {
    var generation = 0
    while (!gameIsOver()) {
        step()
        println("Generation ${++generation}:\n${prettifySystem(system)}")
    }
}

private fun playForSteps(highlife: Highlife, steps: Int) = with(highlife) {
    for (i in 1..steps) {
        if (gameIsOver()) break
        step()
        println("Generation $i:\n${prettifySystem(system)}")
    }
}

private fun play(highlife: Highlife, steps: Int) = with(highlife) {
    println("Seed:\n${prettifySystem(system)}")
    if (steps == -1) playForever(highlife) else playForSteps(highlife, steps)
    if (gameIsOver()) println("The game is over.")
}

fun main() {
    val steps = read("time steps (\"-1\" will generate until the game is over)", min = -1)
    val columns = read("columns", min = 1)
    val highlife = when (readOption()) {
        SystemOption.RANDOM -> Highlife(read("rows", min = 1), columns)
        SystemOption.SUPPLIED -> Highlife(createSystem(readSystem(columns), columns))
    }
    play(highlife, steps)
}