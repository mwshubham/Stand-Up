package com.kevalpatel2106.standup

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.kevalpatel2106.network.ApiProvider
import com.kevalpatel2106.utils.SharedPrefsProvider
import timber.log.Timber
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by Keval on 07-11-17.
 *
 * Application class for the release application. This will initialize the timber tree.
 */

class SUApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Enable timber
        Timber.plant(ReleaseTree())

        //Initialize shared preference
        SharedPrefsProvider.init(this)

        //Init shetho
        Stetho.initializeWithDefaults(this)

        //Initialize the api module
        ApiProvider.init(this@SUApplication)


        //Initialize firebase.
        FirebaseApp.initializeApp(this@SUApplication)

        //Initialize facebook
        @Suppress("DEPRECATION")
        FacebookSdk.sdkInitialize(this@SUApplication)

        // Initializes Fabric for builds that don't use the debug build type.
//        Fabric.with(this, Crashlytics.Builder()
//                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//                .build())

        //Enable firebase analytics
        FirebaseAnalytics.getInstance(this@SUApplication).setAnalyticsCollectionEnabled(true)
    }
}