package com.aliakseipalianski.myapplication

import android.app.Application
import androidx.room.Room
import com.aliakseipalianski.myapplication.news.NewsService
import com.aliakseipalianski.myapplication.news.database.AppDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, APP_DATABASE).build()
    }

    companion object {

        private const val APP_DATABASE = "APP_DATABASE"


        private lateinit var appDatabase: AppDatabase

        private val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://newsapi.org")
            .client(getHttpClientWithInterceptor())
            .build()

        val searchService: NewsService = retrofit.create(NewsService::class.java)

        private fun getHttpClientWithInterceptor(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        fun getRecentlySearchedDao() = appDatabase.getRecentlySearchedDao()
    }
}