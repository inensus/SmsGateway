package com.gateway.android.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.util.Log
import com.gateway.android.BuildConfig


class Util {
    companion object {

        fun checkConnectivity(context: Context) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            val builder = NetworkRequest.Builder()

            connectivityManager!!.registerNetworkCallback(
                builder.build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        SharedPreferencesWrapper.getInstance().networkState = 1
                    }

                    override fun onLost(network: Network) {
                        SharedPreferencesWrapper.getInstance().networkState = 0
                    }
                }
            )
        }

        fun sendEmail(context: Context) {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("support@codeaven.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Codeaven Support")
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Android Version: " + Build.VERSION.RELEASE + "\n" +
                        "Android Api Level: " + Build.VERSION.SDK_INT + "\n" +
                        "Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\n" +
                        "Application Version: " + BuildConfig.VERSION_NAME + "\n" +
                        "Device Token: " + SharedPreferencesWrapper.getInstance().deviceToken
            )

            try {
                context.startActivity(Intent.createChooser(emailIntent, ""))
            } catch (e: Exception) {
                Log.e(context.javaClass.name, "No Mail Chooser Found")
            }
        }
    }
}