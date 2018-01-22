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

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.VisibleForTesting
import com.kevalpatel2106.common.application.BaseApplication
import com.kevalpatel2106.common.base.arch.BaseViewModel
import com.kevalpatel2106.common.base.arch.ErrorMessage
import com.kevalpatel2106.common.db.DailyActivitySummary
import com.kevalpatel2106.standup.R
import com.kevalpatel2106.standup.SUUtils
import com.kevalpatel2106.standup.diary.di.DaggerDiaryComponent
import com.kevalpatel2106.standup.diary.repo.DiaryRepo
import com.kevalpatel2106.standup.timelineview.TimeLineItem
import com.kevalpatel2106.utils.TimeUtils
import com.kevalpatel2106.utils.annotations.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Keval on 22/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@ViewModel(DetailActivity::class)
class DetailViewModel : BaseViewModel {

    @Inject
    internal lateinit var diaryRepo: DiaryRepo

    @VisibleForTesting
    constructor(diaryRepo: DiaryRepo) {
        this.diaryRepo = diaryRepo
    }

    @Suppress("unused")
    constructor() {
        DaggerDiaryComponent.builder()
                .appComponent(BaseApplication.getApplicationComponent())
                .build()
                .inject(this@DetailViewModel)
    }

    internal val summary = MutableLiveData<DailyActivitySummary>()

    internal val timelineEventsList = MutableLiveData<ArrayList<TimeLineItem>>()

    fun fetchData(dayOfMonth: Int, month: Int, year: Int) {
        addDisposable(diaryRepo.loadSummary(dayOfMonth, month, year)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    blockUi.value = true
                }
                .doOnTerminate {
                    blockUi.value = false

                    if (summary.value == null) {
                        val errorMsg = ErrorMessage("No user activities found for $dayOfMonth ${TimeUtils.getMonthInitials(month)} $year")
                        errorMsg.setErrorBtn(R.string.btn_title_retry, { fetchData(dayOfMonth, month, year) })
                        errorMsg.errorImage = R.drawable.bg_no_item_city
                        errorMessage.value = errorMsg
                    }
                }
                .subscribe({
                    summary.value = it

                    //Prepare the timeline data
                    val timelineItems = ArrayList<TimeLineItem>(it.dayActivity.size)
                    it.dayActivity.forEach { timelineItems.add(SUUtils.createTimeLineItemFromUserActivity(it)) }
                    timelineEventsList.value = timelineItems
                }, {
                    val errorMsg = ErrorMessage(it.message)
                    errorMsg.setErrorBtn(R.string.btn_title_retry, { fetchData(dayOfMonth, month, year) })
                    errorMsg.errorImage = R.drawable.img_no_internet_satellite
                    errorMessage.value = errorMsg
                }))
    }
}

