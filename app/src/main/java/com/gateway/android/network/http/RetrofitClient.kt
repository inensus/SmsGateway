package com.gateway.android.network.http

import com.gateway.android.utils.SharedPreferencesWrapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Client implementation for Square's Retrofit.
 */
class RetrofitClient
/**
 * Retrofit Connection Builder
 */
private constructor() {
    /**
     * Returns Retrofit instance
     *
     * @return Retrofit instance
     */
    val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SharedPreferencesWrapper.getInstance().baseUrl!!)
            .client(client)
            .build()
    }

    companion object {

        private const val DEFAULT_CONNECT_TIMEOUT: Long = 30000
        private const val DEFAULT_READ_TIMEOUT: Long = 30000

        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient =
            instance ?: synchronized(this) {
                instance ?: buildRetrofitClient().also { instance = it }
            }

        private fun buildRetrofitClient() =
            RetrofitClient()
    }
}