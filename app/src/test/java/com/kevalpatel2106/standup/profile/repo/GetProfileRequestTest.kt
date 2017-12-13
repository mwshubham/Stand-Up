package com.kevalpatel2106.standup.profile.repo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
class GetProfileRequestTest {

    @Test
    @Throws(IOException::class)
    fun checkUserId() {
        val profile = GetProfileRequest(12345678)
        assertEquals(profile.userId, 12345678)
    }

    @Test
    @Throws(IOException::class)
    fun checkEquals(){
        val profile = GetProfileRequest(12345678)
        val profile1 = GetProfileRequest(12345678)
        val profile2 = GetProfileRequest(1234567890)

        assertEquals(profile, profile1)
        assertEquals(profile, profile)
        assertNotEquals(profile, null)
        assertNotEquals(profile, profile2)
        assertNotEquals(profile1, profile2)
    }

    @Test
    @Throws(IOException::class)
    fun checkHashcode(){
        val id = 12345678L
        val profile = GetProfileRequest(id)

        assertEquals(profile.hashCode(), id.hashCode())
        assertNotEquals(profile, null)
    }
}