package com.gateway.android

import android.app.Application
import android.telephony.TelephonyManager
import com.crashlytics.android.Crashlytics
import com.gateway.android.utils.SharedPreferencesWrapper
import com.gateway.android.utils.Util
import io.fabric.sdk.android.Fabric

class GatewayApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        SharedPreferencesWrapper.getInstance(this).simState = telephonyManager.simState
        Util.checkConnectivity(this)

        Fabric.with(this, Crashlytics())
    }
}
