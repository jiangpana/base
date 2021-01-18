package com.jansir.core

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val result = Array(10) { row ->
            Array(8) { col ->
                "null"
            }
        }

        println(result[0][3]) // Prints: the String at position 0, 3
    }

}