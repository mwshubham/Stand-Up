package com.kevalpatel2106.standup.authentication.intro

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

/**
 * Created by Kevalpatel2106 on 22-Nov-17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class IntroUiModelTest {

    @Test
    @Throws(IOException::class)
    fun checkIsSuccess() {
        val introUiModel = IntroUiModel(true)
        assertTrue(introUiModel.isSuccess)

        introUiModel.isSuccess = false
        assertFalse(introUiModel.isSuccess)
    }

    @Test
    @Throws(IOException::class)
    fun checkIsNewUser() {
        val introUiModel = IntroUiModel(true)
        assertFalse(introUiModel.isNewUser)

        introUiModel.isNewUser = true
        assertTrue(introUiModel.isNewUser)

        introUiModel.isSuccess = false
        assertFalse(introUiModel.isNewUser)
    }
}