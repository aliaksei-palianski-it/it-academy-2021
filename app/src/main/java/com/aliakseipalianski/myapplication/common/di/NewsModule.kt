package com.aliakseipalianski.myapplication.common.di

import com.aliakseipalianski.myapplication.common.AppDatabase
import com.aliakseipalianski.myapplication.news.model.NewsService
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object NewsModule {

    @ActivityRetainedScoped
    @Provides
    fun recentlySearchedDaoProvider(
        appDatabase: AppDatabase
    ): RecentlySearchedDao = appDatabase.getRecentlySearchedDao()


    @ActivityRetainedScoped
    @Provides
    fun recentlyNewsServiceProvider(
        retrofit: Retrofit
    ): NewsService = retrofit.create(NewsService::class.java)
}

