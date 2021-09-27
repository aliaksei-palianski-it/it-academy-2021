package com.aliakseipalianski.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelUnitTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val searchNewsRepository: ISearchNewsRepository = mockk()
    private val newsItem: NewsItem = NewsItem("", "", "", "", "", "", "")
    private val successResult = Result.success(listOf(newsItem))
    private val errorResult: Result<List<NewsItem>> = Result.failure(Throwable("error"))

    @ExperimentalCoroutinesApi
    @Test
    fun `news search test - case when query is not empty`() = testScope.runBlockingTest {
        val query = "test"
        val recentlySearchedResult = listOf(query)

        coEvery {
            searchNewsRepository.search(query)
        } returns successResult

        coEvery {
            searchNewsRepository.addQueryToRecentlySearched(query)
        } returns recentlySearchedResult

        val viewModel = SearchViewModel(searchNewsRepository, testDispatcher)

        viewModel.search(query)
        delay(1000)

        coVerify { searchNewsRepository.search(query) }
        coVerify { searchNewsRepository.addQueryToRecentlySearched(query) }

        assertEquals(viewModel.searchLiveData.value, listOf(newsItem))
        assertEquals(viewModel.historyLiveData.value, recentlySearchedResult)
        assertEquals(viewModel.errorLiveData.value, null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `news search test - case when query is empty`() = testScope.runBlockingTest {
        val query = ""

        coEvery {
            searchNewsRepository.topHeadlines()
        } returns successResult

        coEvery {
            searchNewsRepository.addQueryToRecentlySearched(query)
        } returns emptyList()

        val viewModel = SearchViewModel(searchNewsRepository, testDispatcher)

        viewModel.search(query)
        delay(1000)

        coVerify { searchNewsRepository.topHeadlines() }

        assertEquals(viewModel.searchLiveData.value, listOf(newsItem))
        assertEquals(viewModel.historyLiveData.value, listOf<String>())
        assertEquals(viewModel.errorLiveData.value, null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `news search test - empty response`() = testScope.runBlockingTest {
        val query = ""

        coEvery {
            searchNewsRepository.topHeadlines()
        } returns errorResult

        val viewModel = SearchViewModel(searchNewsRepository, testDispatcher)

        viewModel.search(query)
        delay(1000)

        coVerify { searchNewsRepository.topHeadlines() }

        assertEquals(viewModel.searchLiveData.value, null)
        assertNotEquals(viewModel.errorLiveData.value, null)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `news search test - case when error received`() = testScope.runBlockingTest {
        val query = ""
        val errorMessage = "test exception"

        coEvery {
            searchNewsRepository.topHeadlines()
        } throws Exception(errorMessage)

        val viewModel = SearchViewModel(searchNewsRepository, testDispatcher)

        viewModel.search(query)
        delay(1000)

        coVerify { searchNewsRepository.topHeadlines() }

        assertEquals(viewModel.searchLiveData.value, null)
        assertEquals(viewModel.errorLiveData.value, "java.lang.Exception: $errorMessage")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get recently searched`() = testScope.runBlockingTest {
        val testQuery = "test query"

        coEvery {
            searchNewsRepository.getAllRecentlySearched()
        } returns listOf(testQuery)

        val viewModel = SearchViewModel(searchNewsRepository, testDispatcher)
        viewModel.getRecentlySearched()

        coVerify { searchNewsRepository.getAllRecentlySearched() }

        assertEquals(viewModel.historyLiveData.value, listOf(testQuery))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `make search and clearHistory`() = testScope.runBlockingTest {
        val query = "test"
        val recentlySearchedResult = listOf(query)

        coEvery {
            searchNewsRepository.search(query)
        } returns successResult

        coEvery {
            searchNewsRepository.addQueryToRecentlySearched(query)
        } returns recentlySearchedResult

        val viewModel = SearchViewModel(searchNewsRepository, testDispatcher)
        viewModel.search(query)

        delay(1000)

        coVerify { searchNewsRepository.search(query) }
        coVerify { searchNewsRepository.addQueryToRecentlySearched(query) }

        assertEquals(viewModel.searchLiveData.value, listOf(newsItem))
        assertEquals(viewModel.historyLiveData.value, recentlySearchedResult)
        assertEquals(viewModel.errorLiveData.value, null)

        viewModel.clearHistory()

        assertEquals(viewModel.historyLiveData.value, emptyList<String>())
    }
}