package com.aliakseipalianski.myapplication.common

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object HttpClientProvider {
    fun createAppHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}