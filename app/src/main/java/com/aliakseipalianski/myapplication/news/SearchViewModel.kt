package com.aliakseipalianski.myapplication.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliakseipalianski.myapplication.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class NewsActivityIntent {
    class LoadNewsActivityIntent(val query: String) : NewsActivityIntent()
    object LoadHistoryActivityIntent : NewsActivityIntent()
    object ClearHistoryActivityIntent : NewsActivityIntent()
}

class SearchViewModel : ViewModel() {

    private val searchNewsRepository =
        SearchNewsRepository(App.searchService, App.getRecentlySearchedDao())
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _stateLiveData.postValue(NewsActivityState(error = t.toString()))
    }

    data class NewsActivityState(
        val newsList: List<NewsItem>? = null,
        val error: Any? = null,
        val historyList: List<String>? = null,
    )

    private val _stateLiveData = MutableLiveData<NewsActivityState>()
    val state: LiveData<NewsActivityState> get() = _stateLiveData

    private var searchJob: Job? = null

    override fun onCleared() {
        super.onCleared()

        searchJob = null
    }

    fun onNewIntent(intent: NewsActivityIntent) {
        when (intent) {
            is NewsActivityIntent.LoadNewsActivityIntent -> search(intent.query)
            is NewsActivityIntent.LoadHistoryActivityIntent -> getRecentlySearched()
            is NewsActivityIntent.ClearHistoryActivityIntent -> clearHistory()
        }
    }

    private fun search(text: CharSequence) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(1000)
            val newsResponse = searchNewsRepository.search(text.toString())
            newsResponse.getOrNull()?.let {
                _stateLiveData.postValue(
                    NewsActivityState(
                        newsList = it,
                        historyList = searchNewsRepository.addQueryToRecentlySearched(
                            text.toString().trim()
                        )
                    ),
                )
            } ?: run {
                _stateLiveData.postValue(
                    NewsActivityState(
                        error = newsResponse.exceptionOrNull()?.message ?: "unexpected exception"
                    )
                )
            }
        }
    }

    private fun getRecentlySearched() {
        viewModelScope.launch(exceptionHandler) {
            _stateLiveData.postValue(
                NewsActivityState(
                    historyList = searchNewsRepository.getAllRecentlySearched()
                ),
            )
        }
    }

    private fun clearHistory() {
        viewModelScope.launch(exceptionHandler) {
            searchNewsRepository.clearRecentlySearched()
            _stateLiveData.postValue(
                NewsActivityState(
                    historyList = searchNewsRepository.getAllRecentlySearched()
                ),
            )
        }
    }
}