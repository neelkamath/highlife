package com.neelkamath.highlife

private enum class SystemOption { RANDOM, SUPPLIED }

private enum class Visualizer { STDOUT, CHART }

/**
 * Returns a [System] from [string] where each row has size [columns].
 *
 * `A`s and `D`s in the string represent [Cell.ALIVE] and [Cell.DEAD] respectively. For example, if [columns] is `4`,
 * and [string] is `AADDDDDA`, the [System] will have two rows and four columns.
 */
private fun createSystem(string: String, columns: Int) =
    string.map { if (it == 'A') Cell.ALIVE else Cell.DEAD }.chunked(columns).map { it.toMutableList() }

private fun read(things: String, min: Int): Int {
    print("Enter the number of $things, or \"r\" for a random amount: ")
    val input = readLine()!!
    if (input == "r") return 100
    val num = input.toIntOrNull() ?: read(things, min)
    return if (num < min) read(things, min) else num
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
    print(
        """
        Enter the seed using A and D to represent alive and dead cells respectively (e.g., "ADDDAA" has two rows, and
        three columns):
        """.trimStart().replace(Regex("""\s+"""), " ")
    )
    val input = readLine()!!
    return if (isValidSystem(input, columns)) input else readSystem(columns)
}

private fun readVisualizer(): Visualizer =
    print("Enter \"s\" to visualize the game via stdout, or \"c\" to visualize via a chart: ").run {
        when (readLine()) {
            "s" -> Visualizer.STDOUT
            "c" -> Visualizer.CHART
            else -> readVisualizer()
        }
    }

fun main() {
    val steps = read("time steps (\"-1\" will generate until the game is over)", min = -1)
    val columns = read("columns", min = 1)
    val highlife = when (readOption()) {
        SystemOption.RANDOM -> Highlife(read("rows", min = 1), columns)
        SystemOption.SUPPLIED -> Highlife(createSystem(readSystem(columns), columns))
    }
    when (readVisualizer()) {
        Visualizer.STDOUT -> playOnStdout(highlife, steps)
        Visualizer.CHART -> GuiPlayer(highlife, steps, read("milliseconds to delay between steps", min = 1).toLong())
    }
}