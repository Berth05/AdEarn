package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AdEarn", appName)
    }

    @Test
    fun `user profile conversion math is accurate`() {
        val profile = UserProfile(balancePoints = 5000L, currentStreakDays = 4)
        assertEquals(5.0, profile.balanceUsd, 0.001)
        assertEquals(1.10, profile.streakMultiplier, 0.001)
        assertEquals("Gold", profile.tierName)
    }
}
