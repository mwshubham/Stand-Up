package com.kevalpatel2106.standup.reminder.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by Keval on 16/12/17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
class DemoActivityRecognitionReceiver : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {
        val result = ActivityRecognitionResult.extractResult(intent)

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        val detectedActivities = result.probableActivities as ArrayList<DetectedActivity>

        PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit()
                .putString(DemoRecognitionActivity.PREF_KEY, detectedActivitiesToJson(detectedActivities))
                .apply()

        //Sort the activity list by confidante level
        Collections.sort(detectedActivities) { p0, p1 -> p1.confidence - p0.confidence }

        ActivityUpdateNotification.notify(ctx, detectedActivities)
    }

    private fun detectedActivitiesToJson(detectedActivitiesList: ArrayList<DetectedActivity>): String {
        val type = object : TypeToken<ArrayList<DetectedActivity>>() {

        }.type
        return Gson().toJson(detectedActivitiesList, type)
    }

}