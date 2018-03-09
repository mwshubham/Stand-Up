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

package com.standup.app.settings.whitelisting

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevalpatel2106.common.view.BaseTextView
import com.standup.app.settings.R


/**
 * Created by Kevalpatel2106 on 09-Mar-18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
class WhitelistDialog : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_white_list_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<BaseTextView>(R.id.whitelist_app_dialog_btn).setOnClickListener {
            context?.let {
                WhitelistingUtils.requestForWhitelisting(it)
                dialog.dismiss()
            }
        }
    }

    companion object {

        @SuppressLint("NewApi")
        fun showDialog(context: Context, fragmentManager: FragmentManager) {

            if (WhitelistingUtils.shouldOpenWhiteListDialog(context))
                WhitelistDialog().show(fragmentManager, WhitelistDialog::class.simpleName)
        }
    }
}
