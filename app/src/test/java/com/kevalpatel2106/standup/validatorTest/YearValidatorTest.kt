package com.kevalpatel2106.standup.validatorTest

import com.kevalpatel2106.standup.Validator
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Created by Kevalpatel2106 on 04-Dec-17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@RunWith(Parameterized::class)
class YearValidatorTest(private val input: Int, private val expected: Boolean) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            val list = ArrayList<Array<out Any?>>()
            list.add(arrayOf(1900, true))
            list.add(arrayOf(1901, true))
            list.add(arrayOf(2100, true))
            list.add(arrayOf(2099, true))
            list.add(arrayOf(2017, true))
            list.add(arrayOf(1899, false))
            list.add(arrayOf(2101, false))
            list.add(arrayOf(0, false))
            list.add(arrayOf(-1, false))
            return list
        }
    }

    @Test
    fun testIsValidYear() {
        assertEquals(expected, Validator.isValidYear(input))
    }
}