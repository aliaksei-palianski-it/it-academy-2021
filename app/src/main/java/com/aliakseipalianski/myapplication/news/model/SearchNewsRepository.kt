package com.aliakseipalianski.myapplication.news.model

import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedDao
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedItem
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

interface ISearchNewsRepository {
    suspend fun search(query: String): Result<List<NewsItem>>

    suspend fun topHeadlines(): Result<List<NewsItem>>

    suspend fun getAllRecentlySearched(): List<String>

    suspend fun deleteAllRecentlySearched(): List<String>

    suspend fun addQueryToRecentlySearched(query: String): List<String>
}

class SearchNewsRepository(
    private val newsService: NewsService,
    private val recentlySearchedDao: RecentlySearchedDao,
    private val dispatcher: CoroutineDispatcher,
    private val simpleDateFormat: SimpleDateFormat,
) : ISearchNewsRepository {

    private var recentlySearchedList: List<String>? = null

    override suspend fun search(query: String): Result<List<NewsItem>> {
        return withContext(dispatcher) {
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
        return withContext(dispatcher) {
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
            withContext(dispatcher) {
                insertInMemory(query)
                recentlySearchedDao.insert(
                    RecentlySearchedItem(
                        UUID.nameUUIDFromBytes(query.toByteArray()).toString(),
                        query,
                        (System.currentTimeMillis() / 1000L).toInt()
                    )
                )
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

            recentlySearchedList = withContext(dispatcher) {
                recentlySearchedDao.getAll().map { it.query }
            }
        }

        return recentlySearchedList ?: emptyList()
    }

    override suspend fun deleteAllRecentlySearched(): List<String> {
        withContext(dispatcher) {
            recentlySearchedDao.deleteAll()
        }

        recentlySearchedList = recentlySearchedList?.toMutableList()?.let { mutable ->
            mutable.clear()
            mutable
        }

        return recentlySearchedList ?: emptyList()
    }
}