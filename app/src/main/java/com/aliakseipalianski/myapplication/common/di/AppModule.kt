package com.aliakseipalianski.myapplication.common.di

import android.content.Context
import androidx.room.Room
import com.aliakseipalianski.myapplication.common.AppDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import javax.inject.Qualifier
import javax.inject.Singleton


private const val APP_DATABASE = "APP_DATABASE"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun appDatabaseProvider(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE,
        ).build()

    @Singleton
    @Provides
    fun newsRetrofitProvider(okHttpClient: OkHttpClient) = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://newsapi.org")
        .client(okHttpClient)
        .build()

    @Provides
    fun httpClientProvider(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @MainCoroutineDispatcher
    @Provides
    fun mainCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.Main

    @IOCoroutineDispatcher
    @Provides
    fun ioCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.IO

    @YyyyMMddDateFormat
    @Singleton
    @Provides
    fun yyyyMMddDateFormat() = SimpleDateFormat("yyyy.MM.dd")
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YyyyMMddDateFormat

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainCoroutineDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOCoroutineDispatcher