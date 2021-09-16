package com.aliakseipalianski.myapplication

import java.util.*

class MainModel {

    fun doSomethingObservable(): Observable {
        val observer = Observable()
        // do some async operation
        observer.notifyObservers(Any())

        return observer
    }

    fun doSomething(callback: (Any) -> Unit) {
        // do some async operation
        callback.invoke(Any())
    }
}