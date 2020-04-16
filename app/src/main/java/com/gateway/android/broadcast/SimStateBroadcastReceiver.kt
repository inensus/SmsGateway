package com.gateway.android.broadcast

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.gateway.android.utils.SharedPreferencesWrapper

/**
 * A broadcast receiver who listens for Sim State changes
 */

class SimStateBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val telephonyManager = context.getSystemService(Application.TELEPHONY_SERVICE) as TelephonyManager

        SharedPreferencesWrapper.getInstance(context).simState = telephonyManager.simState
    }
}
