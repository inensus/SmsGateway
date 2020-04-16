package com.gateway.android.firebase

import android.telephony.SmsManager
import android.telephony.TelephonyManager
import com.gateway.android.utils.SharedPreferencesWrapper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.IntentFilter
import android.app.Activity
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.app.PendingIntent

class GatewayFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val message: String = when (resultCode) {
                    Activity.RESULT_OK -> "SMS sent successfully"
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> "RESULT_ERROR_GENERIC_FAILURE"
                    SmsManager.RESULT_ERROR_NO_SERVICE -> "RESULT_ERROR_NO_SERVICE"
                    SmsManager.RESULT_ERROR_NULL_PDU -> "RESULT_ERROR_NULL_PDU"
                    SmsManager.RESULT_ERROR_RADIO_OFF -> "RESULT_ERROR_RADIO_OFF"
                    else -> "Some other error occurred while sending"
                }

                val errorCode = intent?.getIntExtra("errorCode", -1)

                //TODO Send error to server
            }
        }, IntentFilter(SMS_SENT))

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        SharedPreferencesWrapper.getInstance()
                            .sentMessageCount =
                            SharedPreferencesWrapper.getInstance().sentMessageCount!! + 1
                    }
                    else -> {
                        //TODO Send error to server
                    }
                }
            }
        }, IntentFilter(SMS_DELIVERED))

        val number = remoteMessage.data[KEY_NOTIFICATION_EXTRA_NUMBER]
        val message = remoteMessage.data[KEY_NOTIFICATION_EXTRA_MESSAGE]

        if (SharedPreferencesWrapper.getInstance().simState == TelephonyManager.SIM_STATE_READY) {
            try {
                val sentIntent = PendingIntent.getBroadcast(this, 0, Intent(SMS_SENT), 0)
                val deliveryIntent =
                    PendingIntent.getBroadcast(this, 0, Intent(SMS_DELIVERED), 0)

                SmsManager.getDefault()
                    .sendTextMessage(number, null, message, sentIntent, deliveryIntent)
            } catch (e: Exception) {
                //TODO Send error to server
            }
        } else {
            //TODO Send error to server
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        SharedPreferencesWrapper.getInstance().deviceToken = p0
    }

    companion object {
        private const val KEY_NOTIFICATION_EXTRA_NUMBER = "number"
        private const val KEY_NOTIFICATION_EXTRA_MESSAGE = "message"

        private const val SMS_SENT = "SMS_SENT"
        private const val SMS_DELIVERED = "SMS_DELIVERED"
    }
}
