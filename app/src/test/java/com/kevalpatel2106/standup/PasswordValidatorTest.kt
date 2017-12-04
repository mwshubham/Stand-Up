package com.kevalpatel2106.standup

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
class PasswordValidatorTest(private val input: String?, private val expected: Boolean) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): ArrayList<Array<out Any?>> {
            return arrayListOf(
                    arrayOf(null, false),
                    arrayOf("123456", false),
                    arrayOf("123456789", true),
                    arrayOf("1234567890123456789", false),
                    arrayOf("", false)
            )
        }
    }

    @Test
    fun testEmail() {
        assertEquals(expected, Validator.isValidPassword(input))
    }
}