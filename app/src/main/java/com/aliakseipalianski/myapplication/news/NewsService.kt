package com.aliakseipalianski.myapplication.news

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "a04c29db750443398cbe63af8f9cdc75"

interface NewsService {

    @GET("v2/everything")
    fun searchAsync(
        @Query("q") query: String,
        @Query("sortBy") sort: String = "popularity",
        @Query("apiKey") apiKey: String = API_KEY,
    ): Deferred<Response<SearchResponse>>

    @GET("v2/top-headlines")
    fun topHeadlinesAsync(
        @Query("category") category: String = "technology",
        @Query("apiKey") apiKey: String = API_KEY
    ): Deferred<Response<SearchResponse>>
}