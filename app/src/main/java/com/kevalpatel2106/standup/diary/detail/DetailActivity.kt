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

package com.kevalpatel2106.standup.diary.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kevalpatel2106.common.Validator
import com.kevalpatel2106.common.base.uiController.BaseActivity
import com.kevalpatel2106.standup.R
import com.kevalpatel2106.standup.diary.userActivityList.UserActivityListActivity
import com.kevalpatel2106.standup.setPieChart
import com.kevalpatel2106.standup.setPieChartData
import com.kevalpatel2106.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_home_efficiency_card.*
import kotlinx.android.synthetic.main.layout_home_timeline_card.*
import kotlinx.android.synthetic.main.layout_open_user_activity_card.*

class DetailActivity : BaseActivity() {

    private lateinit var model: DetailViewModel

    private var dayOfMonth: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseArguments()
        setContentView(R.layout.activity_detail)
        setToolbar(R.id.toolbar, "$dayOfMonth ${TimeUtils.getMonthInitials(month)} $year", true)

        detail_efficiency_card_pie_chart.setPieChart(this@DetailActivity)
        detail_efficiency_card_pie_chart.setPieChartData(this@DetailActivity, 0F, 0F)

        model = ViewModelProviders.of(this@DetailActivity).get(DetailViewModel::class.java)
        model.blockUi.observe(this@DetailActivity, Observer {
            it?.let {
                if (it) {
                    detail_view_flipper.displayedChild = 1  /* Display loader */
                }
            }
        })
        model.errorMessage.observe(this@DetailActivity, Observer {
            it?.let {
                detail_error_view.setError(it)
                detail_view_flipper.displayedChild = 2  /* Display error */
            }
        })
        model.summary.observe(this@DetailActivity, Observer {
            it?.let {
                //Display summary
                detail_view_flipper.displayedChild = 0

                detail_efficiency_card_pie_chart.setPieChartData(context = this@DetailActivity,
                        sittingDurationPercent = it.sittingPercent,
                        standingDurationPercent = it.standingPercent)

                tracked_time_tv.text = it.durationTimeHours
                total_standing_time_tv.text = it.standingTimeHours
                total_sitting_time_tv.text = it.sittingTimeHours
            }
        })

        //Observe events for time line card
        model.timelineEventsList.observe(this@DetailActivity, Observer {
            it?.let {
                time_line_card.visibility = View.VISIBLE
                today_time_line.timelineItems = it
            }
        })

        view_user_activity_card.setOnClickListener {
            UserActivityListActivity.launch(this@DetailActivity, dayOfMonth, month, year)
        }

        if (savedInstanceState == null) {
            model.fetchData(dayOfMonth, month, year)
        }
    }

    private fun parseArguments() {
        //Parse the arguments
        if (!intent.hasExtra(ARG_DAY_OF_MONTH) || !intent.hasExtra(ARG_MONTH)
                || !intent.hasExtra(ARG_YEAR)) {
            throw IllegalStateException("Provide proper arguments with day of month, month and year.")
        }

        //Get the date
        dayOfMonth = intent.getIntExtra(ARG_DAY_OF_MONTH, -1)
        if (!Validator.isValidDate(dayOfMonth)) {
            throw IllegalArgumentException("Invalid day of month : $dayOfMonth.")
        }

        //Get the month
        month = intent.getIntExtra(ARG_MONTH, -1)
        if (!Validator.isValidMonth(month)) {
            throw IllegalArgumentException("Invalid month : $month.")
        }

        //Get the year
        year = intent.getIntExtra(ARG_YEAR, -1)
        if (!Validator.isValidYear(year)) {
            throw IllegalArgumentException("Invalid year : $year.")
        }
    }

    companion object {
        private const val ARG_DAY_OF_MONTH = "arg_day_of_month"
        private const val ARG_MONTH = "arg_month"
        private const val ARG_YEAR = "arg_year"

        fun launch(context: Context,
                   dayOfMonth: Int,
                   monthOfYear: Int,
                   year: Int) {

            context.startActivity(Intent(context, DetailActivity::class.java).apply {
                putExtra(ARG_DAY_OF_MONTH, dayOfMonth)
                putExtra(ARG_MONTH, monthOfYear)
                putExtra(ARG_YEAR, year)
            })
        }
    }
}
