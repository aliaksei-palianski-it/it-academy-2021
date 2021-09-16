package com.aliakseipalianski.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainModel {

    private val _changedState = MutableLiveData<Any>()

    val changedState: LiveData<Any>
        get() = _changedState

    fun onTextClicked() {
        // load content and change live data state
        // some async manipulate
        _changedState.postValue("some content or moodel")
    }

    fun someThingElse() {

    }

    fun onActionReceived() {

    }

    fun doSomeAction() {

    }

    fun search(text: String) {
            //do async search and change lived
    }
}