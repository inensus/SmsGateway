package com.gateway.android.utils

import android.content.Context
import android.content.SharedPreferences
import android.telephony.TelephonyManager

/**
 * Keeps a reference to the SharedPreference
 * Acts as a Singleton class
 */

class SharedPreferencesWrapper() {

    private lateinit var mSharedPreferences: SharedPreferences
    private var mListener: Listener? = null

    constructor(context: Context) : this() {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    var baseUrl: String?
        get() = mSharedPreferences.getString(KEY_BASE_URL, DEFAULT_BASE_URL)
        set(url) {
            mSharedPreferences.edit().putString(KEY_BASE_URL, url).apply()
        }

    var deviceToken: String?
        get() = mSharedPreferences.getString(KEY_DEVICE_TOKEN, "")
        set(token) {
            mSharedPreferences.edit().putString(KEY_DEVICE_TOKEN, token).apply()
        }

    var sentMessageCount: Int?
        get() = mSharedPreferences.getInt(KEY_SENT_MESSAGE_COUNT, 0)
        set(count) {
            if (count != null) {
                mSharedPreferences.edit().putInt(KEY_SENT_MESSAGE_COUNT, count).apply()
                mListener?.onSharedPreferencesValueChange()
            }
        }

    var receivedMessageCount: Int?
        get() = mSharedPreferences.getInt(KEY_RECEIVED_MESSAGE_COUNT, 0)
        set(count) {
            if (count != null) {
                mSharedPreferences.edit().putInt(KEY_RECEIVED_MESSAGE_COUNT, count).apply()
                mListener?.onSharedPreferencesValueChange()
            }
        }

    var simState: Int?
        get() = mSharedPreferences.getInt(KEY_SIM_STATE, TelephonyManager.SIM_STATE_READY)
        set(state) {
            if (state != null) {
                mSharedPreferences.edit().putInt(KEY_SIM_STATE, state).apply()
                mListener?.onSharedPreferencesValueChange()
            }
        }

    var networkState: Int?
        get() = mSharedPreferences.getInt(KEY_NETWORK_STATE, 1)
        set(state) {
            if (state != null) {
                mSharedPreferences.edit().putInt(KEY_NETWORK_STATE, state).apply()
                mListener?.onSharedPreferencesValueChange()
            }
        }

    companion object {
        const val DEFAULT_BASE_URL = "http://demo.micropowermanager.com/api/"

        private const val SHARED_PREFERENCES_NAME = "inensus-gateway"
        private const val KEY_BASE_URL = "baseUrl"
        private const val KEY_DEVICE_TOKEN = "deviceToken"
        private const val KEY_SENT_MESSAGE_COUNT = "sentMessageCount"
        private const val KEY_RECEIVED_MESSAGE_COUNT = "receivedMessageCount"
        private const val KEY_SIM_STATE = "simState"
        private const val KEY_NETWORK_STATE = "networkState"

        @Volatile
        private var instance: SharedPreferencesWrapper? = null

        fun getInstance(context: Context? = null): SharedPreferencesWrapper =
            SharedPreferencesWrapper.instance ?: synchronized(this) {
                SharedPreferencesWrapper.instance
                    ?: buildSharedPreferencesWrapper(context!!).also { SharedPreferencesWrapper.instance = it }
            }

        private fun buildSharedPreferencesWrapper(context: Context) =
            SharedPreferencesWrapper(context)
    }

    interface Listener {
        fun onSharedPreferencesValueChange()
    }
}
