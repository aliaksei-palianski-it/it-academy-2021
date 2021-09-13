package com.aliakseipalianski.myapplication.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat


class SearchNewsRepository(
    private val newsService: NewsService,
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
) {

    suspend fun search(query: String): Result<List<NewsItem>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                newsService.searchAsync(query = query)
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.articleList?.map {
                        it.toNewsItem(simpleDateFormat)
                    } ?: throw Exception("Empty data")
            }
        }
    }
    // fun searchRx(query: String): Single<List<Any>> = Single.just(emptyList())
}