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

package com.kevalpatel2106.common.di

import com.kevalpatel2106.common.purchase.PurchaseViewModel
import com.kevalpatel2106.utils.annotations.ApplicationScope
import com.standup.app.billing.di.BillingDaggerModule
import dagger.Component

/**
 * Created by Kevalpatel2106 on 20-Mar-18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@ApplicationScope
@Component(dependencies = [AppComponent::class], modules = [CommonsDaggerModule::class, BillingDaggerModule::class])
internal interface CommonsComponent {

    fun inject(purchaseViewModel: PurchaseViewModel)
}
