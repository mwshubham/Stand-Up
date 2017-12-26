package com.kevalpatel2106.base.arch

import android.content.Context
import com.kevalpatel2106.base.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import java.io.IOException

/**
 * Created by Keval on 26/12/17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class ErrorMessageTest {

    @Test
    @Throws(IOException::class)
    fun checkInitWithStringMessage() {
        val errorMessage = ErrorMessage("This is test.")
        Assert.assertEquals(errorMessage.errorMessage, "This is test.")
        Assert.assertEquals(errorMessage.errorRes, 0)
    }

    @Test
    @Throws(IOException::class)
    fun checkInitWithStringRes() {
        val errorMessage = ErrorMessage(R.string.error_activity_not_found)
        Assert.assertEquals(errorMessage.errorRes, R.string.error_activity_not_found)
        Assert.assertNull(errorMessage.errorMessage)
    }

    @Test
    @Throws(IOException::class)
    fun checkErrorBtn() {
        val errorMessage = ErrorMessage(R.string.error_activity_not_found)
        errorMessage.setErrorBtn(R.string.btn_title_retry, {})

        Assert.assertEquals(errorMessage.getErrorBtnText(), R.string.btn_title_retry)
        Assert.assertNotNull(errorMessage.getOnErrorClick())
    }

    @Test
    @Throws(IOException::class)
    fun checkGetErrorMessageWithStringRes() {
        val errorMessage = ErrorMessage(R.string.error_activity_not_found)
        Assert.assertNull(errorMessage.getMessage(null))

        val mockContext = Mockito.mock(Context::class.java)
        Mockito.`when`(mockContext.getString(anyInt())).thenReturn("This is test error.")
        Assert.assertEquals(errorMessage.getMessage(mockContext), "This is test error.")
    }

    @Test
    @Throws(IOException::class)
    fun checkGetErrorMessageWithString() {
        val errorMessage = ErrorMessage("This is test error.")
        Assert.assertEquals(errorMessage.getMessage(null), "This is test error.")
    }
}