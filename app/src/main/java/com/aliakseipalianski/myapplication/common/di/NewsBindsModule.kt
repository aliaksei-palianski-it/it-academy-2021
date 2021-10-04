package com.aliakseipalianski.myapplication.common.di

import androidx.lifecycle.ViewModel
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.model.SearchNewsRepository
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class NewsBindsModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSearchNewsRepository(
        searchNewsRepository: SearchNewsRepository
    ): ISearchNewsRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun bindSearchViewModel(
        searchViewModel: SearchViewModel
    ): ViewModel
}