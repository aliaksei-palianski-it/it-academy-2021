package com.aliakseipalianski.myapplication.news.model

import com.aliakseipalianski.myapplication.common.di.IOCoroutineDispatcher
import com.aliakseipalianski.myapplication.common.di.YyyyMMddDateFormat
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedDao
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedItem
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.random.Random

interface ISearchNewsRepository {
    suspend fun search(query: String): Result<List<NewsItem>>

    suspend fun topHeadlines(): Result<List<NewsItem>>

    suspend fun getAllRecentlySearched(): List<String>

    suspend fun addQueryToRecentlySearched(query: String): List<String>
}

class SearchNewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val recentlySearchedDao: RecentlySearchedDao,
    @YyyyMMddDateFormat private val simpleDateFormat: SimpleDateFormat,
    @IOCoroutineDispatcher private val dispatcher: CoroutineDispatcher,
) : ISearchNewsRepository {

    private var recentlySearchedList: List<String>? = null

    override suspend fun search(query: String): Result<List<NewsItem>> {
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

    override suspend fun topHeadlines(): Result<List<NewsItem>> {
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

    override suspend fun addQueryToRecentlySearched(query: String): List<String> {
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

    override suspend fun getAllRecentlySearched(): List<String> {
        if (recentlySearchedList == null) {

            recentlySearchedList = withContext(Dispatchers.IO) {
                recentlySearchedDao.getAll().map { it.query }
            }
        }

        return recentlySearchedList ?: emptyList()
    }
}