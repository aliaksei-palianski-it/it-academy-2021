package com.aliakseipalianski.myapplication.news.model

import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedDao
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import kotlin.random.Random


class SearchNewsRepository(
    private val newsService: NewsService,
    private val recentlySearchedDao: RecentlySearchedDao,
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
) {

    private var recentlySearchedList: List<String>? = null

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

    suspend fun topHeadlines(): Result<List<NewsItem>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                newsService.topHeadlinesAsync()
                    .await()
                    .takeIf { it.isSuccessful }
                    ?.body()
                    ?.articleList?.map {
                        it.toNewsItem(simpleDateFormat)
                    } ?: throw Exception("Empty data")
            }
        }
    }

    suspend fun addQueryToRecentlySearched(query: String): List<String> {
        if (query.isNotBlank())
            withContext(Dispatchers.IO) {
                insertInMemory(query)
                recentlySearchedDao.insert(RecentlySearchedItem(Random.nextInt(), query))
            }

        return recentlySearchedList ?: emptyList()
    }

    private fun insertInMemory(query: String) {
        recentlySearchedList = recentlySearchedList?.toMutableList()?.let { history ->
            if (history.contains(query))
                history.remove(query)
            history.add(0, query)
            history
        } ?: arrayListOf(query)
    }

    suspend fun getAllRecentlySearched(): List<String> {
        if (recentlySearchedList == null) {

            recentlySearchedList = withContext(Dispatchers.IO) {
                recentlySearchedDao.getAll().map { it.query }
            }
        }

        return recentlySearchedList ?: emptyList()
    }
}