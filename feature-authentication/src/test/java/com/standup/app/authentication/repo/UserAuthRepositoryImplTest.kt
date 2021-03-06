/*
 *  Copyright 2018 Keval Patel.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.standup.app.authentication.repo

import com.kevalpatel2106.network.NetworkApi
import com.kevalpatel2106.testutils.MockServerManager
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.io.IOException

/**
 * Created by Kevalpatel2106 on 06-Dec-17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class UserAuthRepositoryImplTest {
    private val mockServerManager = MockServerManager()
    private lateinit var userAuthRepositoryImpl: UserAuthRepositoryImpl

    @Before
    fun setUp() {
        mockServerManager.startMockWebServer()
        userAuthRepositoryImpl = UserAuthRepositoryImpl(
                NetworkApi().getRetrofitClient(mockServerManager.getBaseUrl())
        )
    }

    @After
    fun tearUp() {
        mockServerManager.close()
    }

    @Test
    @Throws(IOException::class)
    fun checkLogoutSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/logout_success.json"))

        val testSubscriber = TestSubscriber<LogoutResponse>()
        userAuthRepositoryImpl
                .logout(LogoutRequest(5639445604728832, "64df48e6-45de-4bb5-879d-4c1a722f23fd"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNoErrors()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.deviceId == "64df48e6-45de-4bb5-879d-4c1a722f23fd"
                            && t.uid == 5639445604728832
                }
                .assertComplete()
    }

    @Test
    @Throws(IOException::class)
    fun checkLogoutFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<LogoutResponse>()
        userAuthRepositoryImpl
                .logout(LogoutRequest(5639445604728832, "64df48e6-45de-4bb5-879d-4c1a722f23fd"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    @Throws(IOException::class)
    fun checkVerifyEmailSuccess() {
        mockServerManager.enqueueResponse(response = File(mockServerManager.getResponsesPath() + "/email_verify_success.html"),
                type = "text/html")

        val testSubscriber = TestSubscriber<String>()
        userAuthRepositoryImpl
                .verifyEmailLink(mockServerManager.getBaseUrl() + "/verifyEmailLink/32894723874/dskfhj-sdf-vcx-cx-vczx")
                .subscribe(testSubscriber)

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue { it.contains("Your email has been verified.") }
    }

    @Test
    @Throws(IOException::class)
    fun checkForgotPasswordSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/forgot_password_success.json"))

        val testSubscriber = TestSubscriber<ForgotPasswordResponse>()
        userAuthRepositoryImpl
                .forgotPassword(ForgotPasswordRequest("test@example.com"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
    }

    @Test
    @Throws(IOException::class)
    fun checkForgotPasswordFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<ForgotPasswordResponse>()
        userAuthRepositoryImpl
                .forgotPassword(ForgotPasswordRequest("test@example.com"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    @Throws(IOException::class)
    fun checkResendVerifyEmailSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/resend_verification_email_success.json"))

        val testSubscriber = TestSubscriber<ResendVerificationResponse>()
        userAuthRepositoryImpl
                .resendVerifyEmail(ResendVerificationRequest(1234567890))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
    }

    @Test
    @Throws(IOException::class)
    fun checkResendVerifyEmailFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<ResendVerificationResponse>()
        userAuthRepositoryImpl
                .resendVerifyEmail(ResendVerificationRequest(1234567890))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    @Throws(IOException::class)
    fun checkRegisterDeviceSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/device_reg_success.json"))

        val testSubscriber = TestSubscriber<DeviceRegisterResponse>()
        userAuthRepositoryImpl
                .registerDevice(DeviceRegisterRequest(12L,
                        "64df48e6-45de-4bb5-879d-4c1a722f23fd",
                        "64df48e6-45de-4bb5-879d-4c1a722f23fd-yreuiyh-2837bmsf"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.token == "64df48e6-45de-4bb5-879d-4c1a722f23fd" && t.userId == 5639445604728832
                }
    }

    @Test
    @Throws(IOException::class)
    fun checkRegisterDeviceFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<DeviceRegisterResponse>()
        userAuthRepositoryImpl
                .registerDevice(DeviceRegisterRequest(12L,
                        "64df48e6-45de-4bb5-879d-4c1a722f23fd",
                        "64df48e6-45de-4bb5-879d-4c1a722f23fd-yreuiyh-2837bmsf"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    @Throws(IOException::class)
    fun checkLoginSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/user_login_success.json"))

        val testSubscriber = TestSubscriber<LoginResponse>()
        userAuthRepositoryImpl
                .login(LoginRequest("test@example.com", "12345678"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.isVerified
                            && t.photoUrl == "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"
                            && t.email == "test@example.com"
                            && t.name == "Test user"
                            && t.uid == 5629499534213120
                }
    }

    @Test
    @Throws(IOException::class)
    fun checkLoginFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<LoginResponse>()
        userAuthRepositoryImpl
                .login(LoginRequest("test@example.com", "12345678"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    @Throws(IOException::class)
    fun checkSignUpSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/user_sign_up_success.json"))

        val testSubscriber = TestSubscriber<SignUpResponse>()
        userAuthRepositoryImpl
                .signUp(SignUpRequest("test@example.com",
                        "Test User",
                        "12345678",
                        "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.isVerified
                            && t.photoUrl == "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"
                            && t.email == "test@example.com"
                            && t.name == "Test user"
                            && t.uid == 5629499534213120
                            && t.isNewUser
                }
    }

    @Test
    @Throws(IOException::class)
    fun checkSignUpFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<SignUpResponse>()
        userAuthRepositoryImpl
                .signUp(SignUpRequest("test@example.com",
                        "Test User",
                        "12345678",
                        "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    @Throws(IOException::class)
    fun checkSocialSignUpSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/social_user_sign_up_success.json"))

        val testSubscriber = TestSubscriber<SignUpResponse>()
        userAuthRepositoryImpl
                .socialSignUp(SignUpRequest("test@example.com",
                        "Test User",
                        "12345678",
                        "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.isVerified
                            && t.photoUrl == "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"
                            && t.email == "test@example.com"
                            && t.name == "Test user"
                            && t.uid == 5629499534213120
                            && t.isNewUser
                }
    }

    @Test
    @Throws(IOException::class)
    fun checkSocialLoginSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/social_user_login_success.json"))

        val testSubscriber = TestSubscriber<SignUpResponse>()
        userAuthRepositoryImpl
                .socialSignUp(SignUpRequest("test@example.com",
                        "Test User",
                        "12345678",
                        "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.isVerified
                            && t.photoUrl == "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"
                            && t.email == "test@example.com"
                            && t.name == "Test user"
                            && t.uid == 5629499534213120
                            && !t.isNewUser
                }
    }

    @Test
    @Throws(IOException::class)
    fun checkSocialSignUpFail() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath() + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<SignUpResponse>()
        userAuthRepositoryImpl
                .socialSignUp(SignUpRequest("test@example.com",
                        "Test User",
                        "12345678",
                        "https://lh4.googleusercontent.com/-q8RGEpH6lEk/AAAAAAAAAAI/AAAAAAAAdXo/3Ddnk6Ck2bA/s96-c/photo.jpg"))
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber
                .assertError { t -> t.message == "Required field missing." }
                .assertValueCount(0)
    }
}
