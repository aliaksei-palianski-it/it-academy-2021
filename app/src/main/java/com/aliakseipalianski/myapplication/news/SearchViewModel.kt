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

class SearchViewModel : ViewModel() {

    private val searchNewsRepository =
        SearchNewsRepository(App.searchService, App.getRecentlySearchedDao())
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

    fun search(text: CharSequence) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(1000)
            val newsResponse = searchNewsRepository.search(text.toString())
            newsResponse.getOrNull()?.let {
                _searchLiveData.postValue(it)
                _historyLiveData.postValue(
                    searchNewsRepository.addQueryToRecentlySearched(
                        text.toString().trim()
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
        viewModelScope.launch(exceptionHandler) {
            _historyLiveData.postValue(searchNewsRepository.getAllRecentlySearched())
        }
    }

    fun clearHistory() {
        _historyLiveData.value?.let {
            it.toMutableList().let { mutable ->
                mutable.clear()
                _historyLiveData.postValue(mutable)
            }
        }
    }
}