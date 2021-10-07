package com.aliakseipalianski.core

import com.aliakseipalianski.core.recentlySearch.network.NewsService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RestApiProvider {
    fun createApi(httpClient: OkHttpClient): NewsService = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://newsapi.org")
        .client(httpClient)
        .build()
        .create(NewsService::class.java)
}