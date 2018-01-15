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

package com.kevalpatel2106.standup

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.kevalpatel2106.standup.application.BaseApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Created by Keval on 07-11-17.
 *
 * Application class for the release application. This will initialize the timber tree.
 */

class SUApplication : BaseApplication() {
    override fun isReleaseBuild() = true

    override fun onCreate() {
        super.onCreate()

        //Enable timber
        Timber.plant(ReleaseTree())

        //Init shetho
        Stetho.initializeWithDefaults(this)

        // Initializes Fabric for builds that don't use the debug build type.
        Fabric.with(this, Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build())
    }
}
