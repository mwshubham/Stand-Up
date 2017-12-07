package com.kevalpatel2106.standup.authentication.deviceReg

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.kevalpatel2106.network.ApiProvider
import com.kevalpatel2106.standup.authentication.repo.UserAuthRepositoryImpl
import com.kevalpatel2106.testutils.BaseTestClass
import com.kevalpatel2106.testutils.MockServerManager
import com.kevalpatel2106.utils.UserSessionManager
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Kevalpatel2106 on 07-Dec-17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(AndroidJUnit4::class)
class DeviceRegisterActivityTest : BaseTestClass() {

    @JvmField
    @Rule
    var rule = ActivityTestRule(DeviceRegisterActivity::class.java, false, false)

    override fun getActivity(): DeviceRegisterActivity? = rule.activity

    private val mockServerManager = MockServerManager()

    @Before
    fun setUp() {
        UserSessionManager.clearUserSession()
        ApiProvider.init(InstrumentationRegistry.getContext())
        mockServerManager.startMockWebServer()
    }

    @After
    fun tearUp() {
//        mockServerManager.close()
        ApiProvider.init()
    }

    @Test
    @Throws(IOException::class)
    fun checkRegistrationFail() {
        mockServerManager.enqueueResponse(mockServerManager.getStringFromFile(InstrumentationRegistry.getContext(),
                com.kevalpatel2106.standup.test.R.raw.authentication_field_missing))

        //Prepare the intent
        val intent = Intent()
        intent.putExtra(DeviceRegisterActivity.ARG_IS_NEW_USER, false)
        intent.putExtra(DeviceRegisterActivity.ARG_IS_VERIFIED, false)
        rule.launchActivity(intent).mModel = DeviceRegViewModel(UserAuthRepositoryImpl(mockServerManager.getBaseUrl()))

        Thread.sleep(2000)
        assertNull(activity!!.mModel.token.value)
        assertEquals(activity!!.mModel.errorMessage.value!!.getMessage(null), "Required field missing.")
    }

    @Test
    @Throws(IOException::class)
    fun checkRegistrationSuccess() {
        mockServerManager.enqueueResponse(mockServerManager.getStringFromFile(InstrumentationRegistry.getContext(),
                com.kevalpatel2106.standup.test.R.raw.device_reg_success))

        //Prepare the intent
        val intent = Intent()
        intent.putExtra(DeviceRegisterActivity.ARG_IS_NEW_USER, false)
        intent.putExtra(DeviceRegisterActivity.ARG_IS_VERIFIED, false)
        rule.launchActivity(intent).mModel = DeviceRegViewModel(UserAuthRepositoryImpl(mockServerManager.getBaseUrl()))

        Thread.sleep(2000)
        assertNull(activity!!.mModel.errorMessage.value)
        assertEquals(activity!!.mModel.token.value, "64df48e6-45de-4bb5-879d-4c1a722f23fd")
    }
}