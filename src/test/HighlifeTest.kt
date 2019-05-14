package com.neelkamath.highlife.test

import com.neelkamath.highlife.Cell
import com.neelkamath.highlife.Highlife
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HighlifeTest {
    @Test
    fun `Attempting to create an empty Highlife using a random seed should throw Exception`() {
        assertFails { Highlife(3, 0) }
    }

    @Test
    fun `Attempting to create an empty Highlife using a custom seed should throw Exception`() {
        assertFails { Highlife(listOf(mutableListOf(), mutableListOf())) }
    }

    @Test
    fun `Attempting to create a Highlife with different numbers of columns in each row should throw Exception`() {
        assertFails { Highlife(listOf(mutableListOf(Cell.ALIVE, Cell.DEAD), mutableListOf(Cell.DEAD))) }
    }

    @Test
    fun `Time steps should progress systems to their next generations`() =
        with(
            Highlife(
                listOf(
                    mutableListOf(Cell.ALIVE, Cell.DEAD, Cell.DEAD, Cell.DEAD),
                    mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.ALIVE, Cell.DEAD),
                    mutableListOf(Cell.DEAD, Cell.DEAD, Cell.ALIVE, Cell.DEAD)
                )
            )
        ) {
            step()
            assertEquals(
                listOf(
                    mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.DEAD, Cell.DEAD),
                    mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.ALIVE, Cell.DEAD),
                    mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.ALIVE, Cell.DEAD)
                ),
                system
            )
            step()
            assertEquals(
                listOf(
                    mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.ALIVE, Cell.DEAD),
                    mutableListOf(Cell.ALIVE, Cell.DEAD, Cell.DEAD, Cell.DEAD),
                    mutableListOf(Cell.DEAD, Cell.ALIVE, Cell.ALIVE, Cell.DEAD)
                ),
                system
            )
        }

    @Test
    fun `The game shouldn't over if there are alive cells in the system`() {
        assertFalse(Highlife(listOf(mutableListOf(Cell.ALIVE), mutableListOf(Cell.DEAD))).gameIsOver())
    }

    @Test
    fun `The game should be over if the system has no alive cells`() {
        assertTrue(Highlife(listOf(mutableListOf(Cell.DEAD), mutableListOf(Cell.DEAD))).gameIsOver())
    }
}
