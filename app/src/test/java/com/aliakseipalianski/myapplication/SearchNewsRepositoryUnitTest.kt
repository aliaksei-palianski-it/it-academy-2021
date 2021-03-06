package com.aliakseipalianski.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aliakseipalianski.myapplication.dependency.KoinTestRuleProvider
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.model.NewsService
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedDao
import com.aliakseipalianski.myapplication.news.model.database.RecentlySearchedItem
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import java.util.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SearchNewsRepositoryUnitTest : AutoCloseKoinTest() {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var koinTestRule = KoinTestRuleProvider.provide()


    private val newsService: NewsService = mockk()
    private val recentlySearchedDao: RecentlySearchedDao = mockk()
    private val searchNewsRepository: ISearchNewsRepository by inject()

    @Before
    fun init() {
        koinTestRule.koin.loadModules(
            listOf(module {
                single { newsService }
                single { recentlySearchedDao }
                single<CoroutineDispatcher>(named("IO")) { testDispatcher }
            }
            ),
            allowOverride = true
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `add query to recently searched - case when query is not blank`() =
        testScope.runBlockingTest {
            val query = "test"
            val recentlySearchedItem = RecentlySearchedItem(
                UUID.nameUUIDFromBytes(query.toByteArray()).toString(),
                query,
                (System.currentTimeMillis() / 1000L).toInt()
            )

            coEvery {
                recentlySearchedDao.insert(recentlySearchedItem)
            } just Runs

            assertEquals(
                listOf(query),
                searchNewsRepository.addQueryToRecentlySearched(
                    query,
                    recentlySearchedItem.timestamp
                )
            )

            coVerify {
                recentlySearchedDao.insert(recentlySearchedItem)
            }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `add query to recently searched - case when query is blank`() =
        testScope.runBlockingTest {
            val query = " "
            val recentlySearchedItem = RecentlySearchedItem(
                UUID.nameUUIDFromBytes(query.toByteArray()).toString(),
                query,
                (System.currentTimeMillis() / 1000L).toInt()
            )

            coEvery {
                recentlySearchedDao.insert(recentlySearchedItem)
            } just Runs

            assertEquals(
                emptyList(),
                searchNewsRepository.addQueryToRecentlySearched(
                    query,
                    recentlySearchedItem.timestamp
                )
            )

            coVerify(exactly = 0) {
                recentlySearchedDao.insert(recentlySearchedItem)
            }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `the query that contains already in memory will be moved to the beginning`() =
        testScope.runBlockingTest {
            val query = "Apple"
            val query2 = "Google"

            val recentlySearchedItem = RecentlySearchedItem(
                UUID.nameUUIDFromBytes(query.toByteArray()).toString(),
                query,
                (System.currentTimeMillis() / 1000L).toInt()
            )
            val recentlySearchedItem2 = RecentlySearchedItem(
                UUID.nameUUIDFromBytes(query2.toByteArray()).toString(),
                query2,
                (System.currentTimeMillis() / 1000L).toInt()
            )

            coEvery {
                recentlySearchedDao.insert(recentlySearchedItem)
            } just Runs

            coEvery {
                recentlySearchedDao.insert(recentlySearchedItem2)
            } just Runs

            assertEquals(
                listOf(query),
                searchNewsRepository.addQueryToRecentlySearched(
                    query,
                    recentlySearchedItem.timestamp
                )
            )

            coVerify {
                recentlySearchedDao.insert(recentlySearchedItem)
            }

            assertEquals(
                listOf(query2, query),
                searchNewsRepository.addQueryToRecentlySearched(
                    query2,
                    recentlySearchedItem2.timestamp
                )
            )
            assertEquals(
                listOf(query, query2),
                searchNewsRepository.addQueryToRecentlySearched(
                    query,
                    recentlySearchedItem.timestamp
                )
            )
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `get all recently searched - case when recentlySearchedList equals null`() =
        testScope.runBlockingTest {
            val query = "Apple"
            val query2 = "Google"

            val recentlySearchedList = listOf(
                RecentlySearchedItem(
                    UUID.nameUUIDFromBytes(query.toByteArray()).toString(),
                    query,
                    (System.currentTimeMillis() / 1000L).toInt()
                ),
                RecentlySearchedItem(
                    UUID.nameUUIDFromBytes(query2.toByteArray()).toString(),
                    query2,
                    (System.currentTimeMillis() / 1000L).toInt()
                )
            )

            coEvery {
                recentlySearchedDao.getAll()
            } returns recentlySearchedList

            assertEquals(
                recentlySearchedList.map { it.query },
                searchNewsRepository.getAllRecentlySearched()
            )

            coVerify {
                recentlySearchedDao.getAll()
            }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `delete all recently searched`() =
        testScope.runBlockingTest {
            coEvery {
                recentlySearchedDao.deleteAll()
            } just Runs

            assertEquals(
                emptyList(),
                searchNewsRepository.deleteAllRecentlySearched()
            )

            coVerify {
                recentlySearchedDao.deleteAll()
            }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `top headlines`() =
        testScope.runBlockingTest {

            coEvery {
                newsService.topHeadlinesAsync()
            } throws Exception("Empty data")

            searchNewsRepository.topHeadlines()

            coVerify {
                newsService.topHeadlinesAsync()
            }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `search`() =
        testScope.runBlockingTest {
            val query = "test"

            coEvery {
                newsService.searchAsync(query = query)
            } throws Exception("Empty data")

            assertEquals(null, searchNewsRepository.search(query).getOrNull())

            coVerify {
                newsService.searchAsync(query = query)
            }
        }
}