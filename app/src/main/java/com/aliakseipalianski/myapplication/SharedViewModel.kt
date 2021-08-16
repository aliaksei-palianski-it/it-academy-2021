package com.aliakseipalianski.myapplication

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    fun setTitle(title: String) {
        val mutableSampleLiveData = MutableLiveData<String>()
        sampleLiveData = mutableSampleLiveData
        Handler().postDelayed({
            mutableSampleLiveData.postValue(title)
        }, 2000)
    }

    var sampleLiveData: LiveData<String>? = null
}