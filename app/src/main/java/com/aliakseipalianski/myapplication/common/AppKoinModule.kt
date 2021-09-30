package com.aliakseipalianski.myapplication.common

import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.model.SearchNewsRepository
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createAppModule() = module {

    factory { HttpClientProvider.createAppHttpClient() }
    factory<ISearchNewsRepository> { SearchNewsRepository(get(), get(), get()) }

    single { RestApiProvider.createApi(get()) }
    single { AppDatabaseProvider.createAppDatabase(androidApplication()) }
    single {
        get<AppDatabase>().getRecentlySearchedDao()
    }
    single { DateFormatProvider.createYYYYMMDD() }
    single<CoroutineDispatcher> { Dispatchers.Main }

    viewModel { SearchViewModel(get(), get()) }
}

