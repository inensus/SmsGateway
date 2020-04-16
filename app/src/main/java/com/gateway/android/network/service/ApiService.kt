package com.gateway.android.network.service

import com.gateway.android.network.model.Sms
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("sms")
    fun sendSms(@Body sms: Sms): Call<ResponseBody>
}