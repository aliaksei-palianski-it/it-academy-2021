package com.aliakseipalianski.core.koin

import com.aliakseipalianski.core.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

fun createCoreModule() = module {
    factory { HttpClientProvider.createAppHttpClient() }

    single { RestApiProvider.createApi(get()) }
    single { AppDatabaseProvider.createAppDatabase(androidApplication()) }
    single {
        get<AppDatabase>().getRecentlySearchedDao()
    }
    single { DateFormatProvider.createYYYYMMDD() }
    single<CoroutineDispatcher> { Dispatchers.Main }
}