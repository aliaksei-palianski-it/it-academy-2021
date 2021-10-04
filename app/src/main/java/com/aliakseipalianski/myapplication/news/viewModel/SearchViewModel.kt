package com.aliakseipalianski.myapplication.news.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import kotlinx.coroutines.*

class SearchViewModel(
    private val searchNewsRepository: ISearchNewsRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    private val _searchLiveData = MutableLiveData<List<NewsItem>>()
    val searchLiveData: LiveData<List<NewsItem>> get() = _searchLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _historyLiveData = MutableLiveData<List<String>>()
    val historyLiveData: LiveData<List<String>> get() = _historyLiveData

    private var searchJob: Job? = null

    override fun onCleared() {
        super.onCleared()

        searchJob = null
    }

    fun search(text: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcher + exceptionHandler) {
            delay(1000)

            val newsResponse = if (text.isBlank()) {
                searchNewsRepository.topHeadlines()
            } else {
                searchNewsRepository.search(text)
            }
            newsResponse.getOrNull()?.let {
                _searchLiveData.postValue(it)
                _historyLiveData.postValue(
                    searchNewsRepository.addQueryToRecentlySearched(
                        text.trim()
                    )
                )
            } ?: run {
                _errorLiveData.postValue(
                    newsResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    fun getRecentlySearched() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _historyLiveData.postValue(searchNewsRepository.getAllRecentlySearched())
        }
    }

    fun clearHistory() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _historyLiveData.postValue(searchNewsRepository.deleteAllRecentlySearched())
        }
    }
}