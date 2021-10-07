package com.aliakseipalianski.myapplication.common

import com.aliakseipalianski.core.DateFormatProvider
import com.aliakseipalianski.core.HttpClientProvider
import com.aliakseipalianski.core.RestApiProvider
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.model.SearchNewsRepository
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createAppModule() = module {

    factory<ISearchNewsRepository> { SearchNewsRepository(get(), get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
}

