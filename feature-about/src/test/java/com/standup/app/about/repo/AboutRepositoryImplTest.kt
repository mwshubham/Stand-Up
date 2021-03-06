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

package com.standup.app.about.repo

import android.content.SharedPreferences
import com.kevalpatel2106.common.prefs.UserSessionManager
import com.kevalpatel2106.network.NetworkApi
import com.kevalpatel2106.testutils.MockServerManager
import com.kevalpatel2106.utils.SharedPrefsProvider
import com.standup.app.about.BuildConfig
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.io.File

/**
 * Created by Kevalpatel2106 on 01-Jan-18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class AboutRepositoryImplTest {
    private val mockServerManager = MockServerManager()
    private lateinit var aboutRepository: AboutRepository

    @Before
    fun setUp() {
        val sharedPrefs = Mockito.mock(SharedPreferences::class.java)
        Mockito.`when`(sharedPrefs.getLong(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(12L)

        val sharedPrefsEditor = Mockito.mock(SharedPreferences.Editor::class.java)
        Mockito.`when`(sharedPrefs.edit()).thenReturn(sharedPrefsEditor)

        mockServerManager.startMockWebServer()
        aboutRepository = AboutRepositoryImpl(
                NetworkApi().getRetrofitClient(mockServerManager.getBaseUrl()),
                UserSessionManager(SharedPrefsProvider(sharedPrefs))
        )
    }

    @After
    fun tearUp() {
        mockServerManager.close()
    }

    @Test
    fun checkGetLatestVersionSuccessUpdateAvailable() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath()
                + "/get_latest_version_success_update_available.json"))

        val testSubscriber = TestSubscriber<CheckVersionResponse>()
        aboutRepository.getLatestVersion().subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNoErrors()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.latestVersionName == "1.0"
                            && t.latestVersionCode == 10000000
                            && t.latestVersionCode > BuildConfig.VERSION_CODE == t.isUpdate
                            && t.releaseNotes == "This is the release note for the version 3."
                }
                .assertComplete()
    }

    @Test
    fun checkGetLatestVersionSuccessLatestVersionAvailable() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath()
                + "/get_latest_version_success_latest_available.json"))

        val testSubscriber = TestSubscriber<CheckVersionResponse>()
        aboutRepository.getLatestVersion().subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNoErrors()
                .assertValueCount(1)
                .assertValueAt(0) { t ->
                    t.latestVersionName == "1.0"
                            && t.latestVersionCode == 1
                            && !t.isUpdate
                            && t.releaseNotes == null
                }
                .assertComplete()
    }

    @Test
    fun checkGetLatestVersionError() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath()
                + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<CheckVersionResponse>()
        aboutRepository.getLatestVersion().subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertError { it.message == "Required field missing." }
                .assertValueCount(0)
    }

    @Test
    fun checkReportIssueSuccess() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath()
                + "/report_issue_success.json"))

        val testSubscriber = TestSubscriber<ReportIssueResponse>()
        aboutRepository.reportIssue("This is test title.",
                "This is test message.", "test_device_id")
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertNoErrors()
                .assertValueCount(1)
                .assertValueAt(0) { t -> t.issueId == 123456789L }
                .assertComplete()
    }

    @Test
    fun checkReportIssueError() {
        mockServerManager.enqueueResponse(File(mockServerManager.getResponsesPath()
                + "/authentication_field_missing.json"))

        val testSubscriber = TestSubscriber<ReportIssueResponse>()
        aboutRepository.reportIssue("This is test title.",
                "This is test message.", "test_device_id")
                .subscribe(testSubscriber)
        testSubscriber.awaitTerminalEvent()

        testSubscriber.assertError { it.message == "Required field missing." }
                .assertValueCount(0)
    }
}
