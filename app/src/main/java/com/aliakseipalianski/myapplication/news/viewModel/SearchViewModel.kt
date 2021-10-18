package com.aliakseipalianski.myapplication.news.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliakseipalianski.myapplication.news.model.ISearchNewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

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


    fun search(text: String) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
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