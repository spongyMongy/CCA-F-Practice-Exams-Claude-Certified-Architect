package com.arslan.ccafprep.presentation.paywall

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.arslan.ccafprep.data.billing.BillingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val billingManager: BillingManager
) : ViewModel() {

    fun buyFullUnlock(activity: Activity) {
        // Trigger the actual Google Play Billing flow
        billingManager.launchPurchase(activity)
    }
}
