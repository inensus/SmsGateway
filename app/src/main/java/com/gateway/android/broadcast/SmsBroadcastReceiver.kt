package com.gateway.android.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.gateway.android.network.http.RetrofitClient
import com.gateway.android.network.model.Sms
import com.gateway.android.network.service.ApiService
import com.gateway.android.utils.SharedPreferencesWrapper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A broadcast receiver who listens for incoming SMS
 */

class SmsBroadcastReceiver : BroadcastReceiver() {

    private var mApiService: ApiService? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsBody = ""
            val senderNumber: String = Telephony.Sms.Intents.getMessagesFromIntent(intent)[0].displayOriginatingAddress

            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody += smsMessage.messageBody
            }

            if (mApiService == null) {
                mApiService = RetrofitClient.getInstance()
                    .retrofit.create(ApiService::class.java)
            }

            SharedPreferencesWrapper.getInstance()
                .receivedMessageCount = SharedPreferencesWrapper.getInstance().receivedMessageCount!! + 1

            mApiService?.sendSms(Sms(senderNumber, smsBody))
                ?.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    }
                })
        }
    }
}
