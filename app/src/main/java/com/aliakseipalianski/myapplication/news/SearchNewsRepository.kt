package com.aliakseipalianski.myapplication.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request


private const val HTTPS = "https"
private const val BASE_NEWS_HOST = "newsapi.org"
private const val NEWS_API_VERSION = "v2"
private const val SEARCH_NEWS = "everything"

private const val PARAM_QUERY = "q"
private const val PARAM_SORT = "sortBy"

private const val PARAM_API_KEY = "apiKey"


class SearchNewsRepository(private val okHttpClient: OkHttpClient) {

    suspend fun search(query: String): Result<String> {
        return withContext(Dispatchers.IO) {
            runCatching {
                HttpUrl.Builder()
                    .scheme(HTTPS)
                    .host(BASE_NEWS_HOST)
                    .addPathSegment(NEWS_API_VERSION)
                    .addPathSegment(SEARCH_NEWS)
                    .addQueryParameter(PARAM_QUERY, query)
                    .addQueryParameter(PARAM_SORT, "popularity")
                    .addQueryParameter(PARAM_API_KEY, "a04c29db750443398cbe63af8f9cdc75")
                    .build()
                    .let {
                        Request.Builder().get().url(it).build()
                    }.let {
                        okHttpClient.newCall(it).execute()
                    }.body?.string() ?: ""
            }
        }
    }

    // fun searchRx(query: String): Single<List<Any>> = Single.just(emptyList())
}