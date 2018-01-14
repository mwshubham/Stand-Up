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

package com.kevalpatel2106.standup.settings.syncing

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import com.kevalpatel2106.standup.R
import com.kevalpatel2106.standup.application.BaseApplication
import com.kevalpatel2106.standup.misc.UserSessionManager
import com.kevalpatel2106.standup.misc.UserSettingsManager
import com.kevalpatel2106.standup.settings.di.DaggerSettingsComponent
import com.kevalpatel2106.standup.settings.findPrefrance
import com.kevalpatel2106.standup.settings.widget.BasePreference
import com.kevalpatel2106.standup.settings.widget.BasePreferenceCategory
import com.kevalpatel2106.standup.settings.widget.BaseSwitchPreference
import javax.inject.Inject

class SyncSettingsFragment : PreferenceFragmentCompat() {

    @Inject internal lateinit var sessionManager: UserSessionManager
    @Inject internal lateinit var settingsManager: UserSettingsManager

    private lateinit var model: SyncSettingsViewModel

    init {
        DaggerSettingsComponent.builder()
                .appComponent(BaseApplication.appComponent)
                .build()
                .inject(this@SyncSettingsFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this@SyncSettingsFragment).get(SyncSettingsViewModel::class.java)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.sync_settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set sync category
        val syncPrefCategory = findPrefrance(R.string.pref_key_sync_category_title) as BasePreferenceCategory
        syncPrefCategory.title = String.format(getString(R.string.pref_title_logged_in_as), sessionManager.email)

        //Set sync now
        val syncNowPref = findPrefrance(R.string.pref_key_sync_now) as BasePreference
        syncNowPref.setOnPreferenceClickListener {
            context?.let { model.syncNow(it) }
            return@setOnPreferenceClickListener true
        }
        model.isSyncing.observe(this@SyncSettingsFragment, Observer {
            syncNowPref.summary = getString(R.string.pref_syncing)
        })
        model.lastSyncTime.observe(this@SyncSettingsFragment, Observer {
            it?.let {
                if (it.isEmpty()) {
                    syncNowPref.summary = getString(R.string.pref_summary_sync_now_never_synced)
                } else {
                    syncNowPref.summary = String.format(getString(R.string.pref_summary_sync_now), it)
                }
            }
        })

        //Set background sync
        val backgroundSyncPref = findPrefrance(R.string.pref_key_background_sync) as BaseSwitchPreference
        backgroundSyncPref.isChecked = settingsManager.enableBackgroundSync //Set initial value
    }

    companion object {
        fun getNewInstance(): SyncSettingsFragment {
            return SyncSettingsFragment()
        }
    }
}