package com.kevalpatel2106.standup.authentication.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevalpatel2106.facebookauth.FacebookUser
import com.kevalpatel2106.googleauth.GoogleAuthUser
import com.kevalpatel2106.standup.R
import com.kevalpatel2106.standup.UnitTestUtils
import com.kevalpatel2106.standup.authentication.repo.MockUserAuthRepository
import com.kevalpatel2106.standup.authentication.repo.SignUpRequest
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.io.IOException


/**
 * Created by Keval on 20/11/17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@RunWith(JUnit4::class)
class LoginViewModelSocialTest {
    private val RESPONSE_DIR_PATH = String.format("%s/app/src/test/java/com/kevalpatel2106/standup/authentication/repo", File(File("").absolutePath))

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel
    private var mTestRepoMock = MockUserAuthRepository()

    companion object {

        @JvmStatic
        @BeforeClass
        fun setGlobal() = UnitTestUtils.initApp()
    }

    @Before
    fun setUp() {
        //Swap the repo
        loginViewModel = LoginViewModel(mTestRepoMock)
    }

    @After
    fun tearUp() {
        mTestRepoMock.close()
    }

    @Test
    @Throws(IOException::class)
    fun checkAuthenticateGoogleUserWithoutEmail() {
        val googleAuthUser = GoogleAuthUser("46753782367943")
        googleAuthUser.email = ""
        googleAuthUser.name = ""
        loginViewModel.authenticateSocialUser(googleAuthUser)

        //There should be success.
        Assert.assertEquals(loginViewModel.errorMessage.value!!.errorRes, R.string.error_google_login_email_not_found)
        Assert.assertFalse(loginViewModel.blockUi.value!!)
    }

    @Test
    @Throws(IOException::class)
    fun checkAuthenticateFbUserWithoutEmail() {
        val fbUser = FacebookUser("46753782367943")
        loginViewModel.authenticateSocialUser(fbUser)

        //There should be success.
        Assert.assertEquals(loginViewModel.errorMessage.value!!.errorRes, R.string.error_fb_login_email_not_found)
        Assert.assertFalse(loginViewModel.blockUi.value!!)
    }

    @Test
    @Throws(IOException::class)
    fun checkAuthenticateSocialUserSignUpSuccess() {
        mTestRepoMock.enqueueResponse(File(RESPONSE_DIR_PATH + "/social_user_sign_up_success.json"))

        //Make the api call to the mock server
        val signInRequest = SignUpRequest("test@example.com", "Test User", null, null)
        loginViewModel.authenticateSocialUser(signInRequest)

        //There should be success.
        Assert.assertFalse(loginViewModel.blockUi.value!!)
        Assert.assertTrue(loginViewModel.mLoginUiModel.value!!.isSuccess)
        Assert.assertTrue(loginViewModel.mLoginUiModel.value!!.isVerify)
        Assert.assertTrue(loginViewModel.mLoginUiModel.value!!.isNewUser)
    }

    @Test
    @Throws(IOException::class)
    fun checkAuthenticateSocialUserLoginSuccess() {
        mTestRepoMock.enqueueResponse(File(RESPONSE_DIR_PATH + "/social_user_login_success.json"))

        //Make the api call to the mock server
        val signInRequest = SignUpRequest("test@example.com", "Test User", null, null)
        loginViewModel.authenticateSocialUser(signInRequest)

        //There should be success.
        Assert.assertFalse(loginViewModel.blockUi.value!!)
        Assert.assertTrue(loginViewModel.mLoginUiModel.value!!.isSuccess)
        Assert.assertTrue(loginViewModel.mLoginUiModel.value!!.isVerify)
        Assert.assertFalse(loginViewModel.mLoginUiModel.value!!.isNewUser)
    }

    @Test
    @Throws(IOException::class)
    fun checkAuthenticateSocialUserFieldMissing() {
        mTestRepoMock.enqueueResponse(File(RESPONSE_DIR_PATH + "/authentication_field_missing.json"))

        //Make the api call to the mock server
        loginViewModel.authenticateSocialUser(SignUpRequest("test@example.com", "Test User", null, null))

        //There should be success.
        Assert.assertFalse(loginViewModel.blockUi.value!!)
        Assert.assertFalse(loginViewModel.mLoginUiModel.value!!.isSuccess)
        Assert.assertEquals(loginViewModel.errorMessage.value!!.getMessage(null), "Required field missing.")
    }
}