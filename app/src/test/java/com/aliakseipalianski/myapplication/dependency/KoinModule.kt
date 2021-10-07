package com.aliakseipalianski.myapplication.dependency

import com.aliakseipalianski.core.DateFormatProvider
import com.aliakseipalianski.core.HttpClientProvider
import com.aliakseipalianski.core.RestApiProvider
import com.aliakseipalianski.myapplication.common.*
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.model.SearchNewsRepository
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


fun testModule() = module {
    factory { HttpClientProvider.createAppHttpClient() }
    factory<ISearchNewsRepository> {
        SearchNewsRepository(
            get(),
            get(),
            get(qualifier = named("IO")),
            get()
        )
    }

    single { RestApiProvider.createApi(get()) }
    single { AppDatabaseProvider.createAppDatabase(androidApplication()) }
    single {
        get<AppDatabase>().getRecentlySearchedDao()
    }
    single { DateFormatProvider.createYYYYMMDD() }
    single<CoroutineDispatcher>(named("Main")) { Dispatchers.Main }
    single<CoroutineDispatcher>(named("IO")) { Dispatchers.IO }

    viewModel { SearchViewModel(get(), get(qualifier = named("Main"))) }
}