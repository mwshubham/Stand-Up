package com.kevalpatel2106.network

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.kevalpatel2106.network.consumer.NWErrorConsumer
import com.kevalpatel2106.network.consumer.NWSuccessConsumer
import com.kevalpatel2106.testutils.BaseTestClass
import com.kevalpatel2106.testutils.MockWebserverUtils
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.net.HttpURLConnection


/**
 * Created by Keval on 12/11/17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */

@RunWith(AndroidJUnit4::class)
class BaseApiWrapperTest : BaseTestClass() {

    @SmallTest
    fun checkBaseUrl() {
        val retrofit = BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient("http://google.com")

        Assert.assertEquals(retrofit.baseUrl().toString(), "http://google.com/")
    }

    @SmallTest
    fun checkOkHttpClient() {
        val okHttpClient = BaseApiWrapper(InstrumentationRegistry.getContext())
                .getOkHttpClientBuilder(0, null, null)

        Assert.assertEquals(okHttpClient.interceptors().size, 2)
        Assert.assertEquals(okHttpClient.readTimeoutMillis(), 60 * 1000)
        Assert.assertEquals(okHttpClient.writeTimeoutMillis(), 60 * 1000)
        Assert.assertEquals(okHttpClient.cache().directory(),
                File(InstrumentationRegistry.getContext().cacheDir, "responses"))
        Assert.assertEquals(okHttpClient.cache().maxSize(), NWInterceptor.CACHE_SIZE)
    }

    @Test
    fun checkPageNotFound() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        //404 response
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, HttpURLConnection.HTTP_NOT_FOUND)
                        Assert.assertEquals(message, APIStatusCodes.ERROR_MESSAGE_NOT_FOUND)
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })

    }

    @Test
    fun checkUnAuthorise() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        //404 response
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, HttpURLConnection.HTTP_UNAUTHORIZED)
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })

    }

    @Test
    fun checkBadRequest() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        //400 response
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, HttpURLConnection.HTTP_BAD_REQUEST)
                        Assert.assertEquals(message, APIStatusCodes.ERROR_MESSAGE_BAD_REQUEST)
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })

    }

    @Suppress("DEPRECATION")
    @Test
    fun checkServerBusy() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        //500 response
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_SERVER_ERROR))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, HttpURLConnection.HTTP_SERVER_ERROR)
                        Assert.assertEquals(message, APIStatusCodes.ERROR_MESSAGE_SERVER_BUSY)
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })

    }

    @Test
    fun checkUnknownResponseCode() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(103))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, 103)
                        Assert.assertEquals(message, APIStatusCodes.ERROR_MESSAGE_SOMETHING_WRONG)
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })
    }

    @Test
    fun checkSuccessResponse() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setHeader("Content-Type", "application/json")
                .setBody(MockWebserverUtils.getStringFromFile(InstrumentationRegistry.getContext(),
                        com.kevalpatel2106.network.test.R.raw.sucess_sample)))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.assertNotNull(data)
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.fail("There shouldn't be error.")
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })

    }

    @Test
    fun checkFieldMissingResponse() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setHeader("Content-Type", "application/json")
                .setBody(MockWebserverUtils.getStringFromFile(InstrumentationRegistry.getContext(),
                        com.kevalpatel2106.network.test.R.raw.required_field_missing_sample)))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, APIStatusCodes.ERROR_CODE_REQUIRED_FIELD_MISSING)
                        Assert.assertEquals(message, "Username is missing.")
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })
    }

    @Test
    fun checkServerExceptionResponse() {
        val mockWebServer = MockWebserverUtils.startMockWebServer()
        mockWebServer.enqueue(MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setHeader("Content-Type", "application/json")
                .setBody(MockWebserverUtils.getStringFromFile(InstrumentationRegistry.getContext(),
                        com.kevalpatel2106.network.test.R.raw.exception_sample)))

        BaseApiWrapper(InstrumentationRegistry.getContext())
                .getRetrofitClient(MockWebserverUtils.getBaseUrl(mockWebServer))
                .create(TestApiService::class.java)
                .callBase()
                .subscribe(object : NWSuccessConsumer<TestData>() {

                    override fun onSuccess(@Suppress("UNUSED_PARAMETER") data: TestData?) {
                        Assert.fail("This cannot give success.")
                    }
                }, object : NWErrorConsumer() {

                    override fun onError(code: Int, message: String) {
                        Assert.assertEquals(code, APIStatusCodes.ERROR_CODE_EXCEPTION)
                        Assert.assertEquals(message, APIStatusCodes.ERROR_MESSAGE_SOMETHING_WRONG)
                    }

                    override fun onInternetUnavailable(message: String) {
                        Assert.fail("Internet is there")
                    }
                })
    }

    override fun getActivity(): Activity? {
        return null
    }
}
