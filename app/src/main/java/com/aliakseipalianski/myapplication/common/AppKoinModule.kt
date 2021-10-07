package com.aliakseipalianski.myapplication.common

import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.model.SearchNewsRepository
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun createAppModule() = module {

    factory<ISearchNewsRepository> {
        SearchNewsRepository(
            get(),
            get(),
            get(qualifier = named("IO")),
            get()
        )
    }
    viewModel { SearchViewModel(get(), get(qualifier = named("Main"))) }
}

