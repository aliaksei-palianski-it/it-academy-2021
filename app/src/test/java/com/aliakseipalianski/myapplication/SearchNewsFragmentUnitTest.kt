package com.aliakseipalianski.myapplication

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aliakseipalianski.myapplication.common.App
import com.aliakseipalianski.myapplication.news.view.SearchNewsFragment
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SearchNewsFragmentUnitTest : AutoCloseKoinTest() {

    private val historyMutableLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private val newsItemMutableLiveData: MutableLiveData<List<NewsItem>> = MutableLiveData()
    private val errorMutableLiveData: MutableLiveData<String> = MutableLiveData()

    private val viewModel: SearchViewModel = mockk()

    private val searchedNewsItemList =
        listOf(
            NewsItem("", "", "", "", "", "", ""),
            NewsItem("", "", "", "", "", "", "")
        )
    private val newsItemList = listOf(NewsItem("", "", "", "", "", "", ""))
    private val recentlySearchedItemList = listOf("")

    private lateinit var scenario: FragmentScenario<SearchNewsFragment>

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Config(application = App::class, packageName = "com.aliakseipalianski.myapplication")
    @Before
    fun initialisation() {
        loadKoinModules(
            module(override = true) {
                factory {
                    viewModel
                }
            }
        )

        val fragment = SearchNewsFragment()

        scenario =
            launchFragmentInContainer(initialState = Lifecycle.State.INITIALIZED) { fragment }

        every {
            viewModel.searchLiveData
        } returns newsItemMutableLiveData

        every {
            viewModel.historyLiveData
        } returns historyMutableLiveData

        every {
            viewModel.errorLiveData
        } returns errorMutableLiveData

        every {
            viewModel.getRecentlySearched()
        } answers {
            historyMutableLiveData.value = recentlySearchedItemList
        }

        every {
            viewModel.search("")
        } answers {
            newsItemMutableLiveData.value = newsItemList
            historyMutableLiveData.value = recentlySearchedItemList
        }

        every {
            viewModel.clearHistory()
        } answers {
            historyMutableLiveData.value = emptyList()
        }
    }

    @Test
    fun `search input - less then 2`() {
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.searchInput)).perform(ViewActions.typeText("qq"))
        onView(withId(R.id.newsRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }
    }

    @Test
    fun `search input`() {
        val query = "qqq"

        every {
            viewModel.search("")
        } answers {
            newsItemMutableLiveData.value = newsItemList
            historyMutableLiveData.value = emptyList()
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.historyRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 0)
        }

        onView(withId(R.id.clearHistory)).check { view, _ ->
            assertEquals(view.visibility, View.GONE)
        }

        onView(withId(R.id.newsRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }

        every {
            viewModel.search(query)
        } answers {
            newsItemMutableLiveData.value = searchedNewsItemList
            historyMutableLiveData.value = recentlySearchedItemList
        }

        onView(withId(R.id.searchInput)).perform(ViewActions.typeText(query))

        onView(withId(R.id.newsRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 2)
        }

        onView(withId(R.id.historyRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }

        onView(withId(R.id.clearHistory)).check { view, _ ->
            assertEquals(view.visibility, View.VISIBLE)
        }
    }

    @Test
    fun `on view create - no history`() {
        val query = ""

        every {
            viewModel.getRecentlySearched()
        } answers {
            historyMutableLiveData.value = emptyList()
        }

        every {
            viewModel.search("")
        } answers {
            newsItemMutableLiveData.value = newsItemList
            historyMutableLiveData.value = emptyList()
        }

        scenario.moveToState(Lifecycle.State.RESUMED)

        verify {
            viewModel.getRecentlySearched()
            viewModel.search(query)
        }

        onView(withId(R.id.historyRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 0)
        }

        onView(withId(R.id.newsRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }
    }

    @Test
    fun `on view create`() {
        val query = ""

        scenario.moveToState(Lifecycle.State.RESUMED)

        verify {
            viewModel.getRecentlySearched()
            viewModel.search(query)
        }

        onView(withId(R.id.historyRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }

        onView(withId(R.id.newsRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }
    }

    @Test
    fun `clear history`() {
        historyMutableLiveData.value = recentlySearchedItemList
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.historyRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 1)
        }

        onView(withId(R.id.clearHistory)).check { view, _ ->
            assertEquals(view.visibility, View.VISIBLE)
        }

        onView(withId(R.id.clearHistory)).perform(ViewActions.click())

        verify {
            viewModel.clearHistory()
        }

        onView(withId(R.id.historyRecycler)).check { view, _ ->
            assertEquals((view as? RecyclerView)?.adapter?.itemCount, 0)
        }

        onView(withId(R.id.clearHistory)).check { view, _ ->
            assertEquals(view.visibility, View.GONE)
        }
    }

}