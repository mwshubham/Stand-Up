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

package com.kevalpatel2106.standup.diary.repo

import android.support.annotation.VisibleForTesting
import com.kevalpatel2106.standup.BuildConfig
import com.kevalpatel2106.standup.db.DailyActivitySummary
import com.kevalpatel2106.standup.db.StandUpDb
import com.kevalpatel2106.standup.db.userActivity.UserActivity
import com.kevalpatel2106.standup.db.userActivity.UserActivityDao
import com.kevalpatel2106.standup.diary.repo.DiaryRepo.Companion.PAGE_SIZE
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Kevalpatel2106 on 25-Dec-17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
class DiaryRepoImpl @VisibleForTesting constructor(private val userActivityDao: UserActivityDao,
                                                   private val baseUrl: String) : DiaryRepo {

    constructor() : this(StandUpDb.getDb().userActivityDao(), BuildConfig.BASE_URL)

    /**
     * Calling this function will load maximum [PAGE_SIZE] number of days summary which is [beforeMills]
     * and emit the summary for each day one-by-one. The emitted days will order by the day of year
     * in descending order.
     *
     * Internally this will load the [UserActivity] data from locally cached database using [loadUserActivityByDay]
     * and or from the network if the database cache is missing. After that, it will process the
     * [UserActivity] and create the summary of the day in [DailyActivitySummary] and emmit the data.
     *
     * @see loadUserActivityByDay
     */
    override fun loadDaysSummaryList(beforeMills: Long): Flowable<DailyActivitySummary> {

        val flowable = Flowable.create(FlowableOnSubscribe<List<UserActivity>> { e ->

            //Setup the calender object.
            val calender = Calendar.getInstance()
            calender.timeInMillis = beforeMills

            //Get the oldest day
            val oldestActivity = userActivityDao.getOldestActivity()

            //Loop until the oldest event received
            while (oldestActivity != null && calender.timeInMillis > oldestActivity.eventStartTimeMills) {
                //Query the database.
                val dbList = ArrayList(loadUserActivityByDay(calender.clone() as Calendar))

                //Emit the list if data is available
                DailyActivitySummary.convertToValidUserActivityList(dbList)
                if (dbList.isNotEmpty()) {
                    e.onNext(dbList)
                }

                //Go one day before.
                calender.add(Calendar.DAY_OF_MONTH, -1)
            }

            //TODO call the server for more events.

            //Nothing from the network
            e.onComplete()
        }, BackpressureStrategy.BUFFER)
                .map(Function<List<UserActivity>, DailyActivitySummary> {
                    return@Function DailyActivitySummary.fromDayActivityList(ArrayList(it))
                })

        return flowable.zipWith(Flowable.range(1, DiaryRepo.PAGE_SIZE)  /*Flowable that emits 1 to 10*/,
                BiFunction<DailyActivitySummary, Int, DailyActivitySummary> { t1, _ ->
                    t1 /* Emit the DailyActivitySummary list */
                })
    }

    override fun loadUserActivityByDay(calendar: Calendar): List<UserActivity> {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTimeMills = calendar.timeInMillis - 1

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endTimeMills = calendar.timeInMillis + 1

        return userActivityDao.getActivityBetweenDuration(startTimeMills, endTimeMills)
    }
}