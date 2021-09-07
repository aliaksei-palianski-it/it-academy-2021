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

    private val searchNewsRepository = SearchNewsRepository(App.httpClient)
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        _errorLiveData.postValue(t.toString())
    }

    private val _searchLiveData = MutableLiveData<String>()
    val searchLiveData: LiveData<String> get() = _searchLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _historyLiveData = MutableLiveData<ArrayList<String>>()
    val historyLiveData: LiveData<ArrayList<String>> get() = _historyLiveData

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
                addToHistory(text.toString().trim())
            } ?: run {
                _errorLiveData.postValue(
                    newsResponse.exceptionOrNull()?.message ?: "unexpected exception"
                )
            }
        }
    }

    private fun addToHistory(query: String) {
        if (query.isBlank()) return
        _historyLiveData.value?.let { history ->
            if (!history.contains(query)) {
                history.add(query)
                _historyLiveData.postValue(history)
            }
        } ?: _historyLiveData.postValue(arrayListOf(query))
    }

    /*
     fun searchRx(text: CharSequence) {
         searchNewsRepository.searchRx(text.toString())
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnSuccess {
                 _searchLiveData.postValue(it)
             }
             .doOnError { }
     }
 */
}