package com.kevalpatel2106.standup.profile.repo

import com.kevalpatel2106.standup.constants.AppConfig
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

/**
 * Created by Kevalpatel2106 on 05-Dec-17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class GetProfileResponseTest {


    @Test
    @Throws(IOException::class)
    fun checkFields() {
        val profile = GetProfileResponse(name = "Test User",
                gender = AppConfig.GENDER_MALE,
                photo = "http://google.com",
                height = "123.45",
                weight = "67.5",
                email = "test@example.com",
                userId = 12345678,
                isVerified = true)

        assertEquals(profile.userId, 12345678)
        assertEquals(profile.photo, "http://google.com")
        assertEquals(profile.email, "test@example.com")
        assertEquals(profile.gender, AppConfig.GENDER_MALE)
        assertEquals(profile.name, "Test User")
        assertEquals(profile.height, "123.45")
        assertEquals(profile.weight, "67.5")
        assertTrue(profile.isVerified)
    }


    @Test
    @Throws(IOException::class)
    fun checkEquals() {
        val profile = GetProfileResponse(name = "Test User",
                gender = AppConfig.GENDER_MALE,
                photo = "http://google.com",
                height = "123.45",
                weight = "67.5",
                email = "test@example.com",
                userId = 12345678,
                isVerified = true)
        val profile1 = GetProfileResponse(name = "Test User",
                gender = AppConfig.GENDER_MALE,
                photo = "http://google.com",
                height = "123.45",
                weight = "67.5",
                email = "test@example.com",
                userId = 12345678,
                isVerified = true)
        val profile2 = GetProfileResponse(name = "Test1 User",
                gender = AppConfig.GENDER_MALE,
                photo = "http://google.com",
                height = "123.45",
                weight = "67.5",
                email = "test1@example.com",
                userId = 12345678,
                isVerified = true)
        assertEquals(profile, profile1)
        assertEquals(profile, profile)
        assertNotEquals(profile, profile2)
        assertNotEquals(profile1, profile2)
        assertNotEquals(profile1, null)
    }
}