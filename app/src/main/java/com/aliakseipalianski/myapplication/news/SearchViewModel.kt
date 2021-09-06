package com.aliakseipalianski.myapplication.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val searchNewsRepository = SearchNewsRepository()

    private val _searchLiveData = MutableLiveData<List<Any>>()
    val searchLiveData: LiveData<List<Any>> get() = _searchLiveData

    override fun onCleared() {
        super.onCleared()
    }
}