package com.aliakseipalianski.myapplication

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NetworkViewModel : ViewModel() {

    private val scheduledThreadPool = Executors.newScheduledThreadPool(2)
    private val singleThreadPool = Executors.newSingleThreadExecutor()
    private val fixedThreadPool = Executors.newFixedThreadPool(2)

    private var _networkLiveData = MutableLiveData<String>()
    val networkLiveData: LiveData<String>
        get() = _networkLiveData


    fun scheduledLoadDataBackground() {
        val delay = Random.nextLong(0, 5000) + 1000

        Log.d("thread", "delay : $delay")

        repeat(5) {
            Log.d("thread", "repeat : $delay")

            scheduledThreadPool.schedule(
                {

                    Log.d("thread", "schedule : $delay")
                    Thread.sleep(200)
                    _networkLiveData.postValue("$it with delay $delay")
                },
                delay,
                TimeUnit.MILLISECONDS
            )
        }
    }

    fun singleThreadExecutorLoadDataBackground() {
        singleThreadPool.execute {
            repeat(5) {
                Thread.sleep(100)
                _networkLiveData.postValue("$it with single thread pool")
            }
        }
    }

    fun fixedThreadPoolLoadDataBackground() {
        val future = fixedThreadPool.submit(Callable<String> {
            repeat(50) {
                Log.d("threads", "Background thread is working! $it")
                _networkLiveData.postValue("$it with fixed")

                Thread.sleep(10)
            }

            return@Callable "Finished"
        })

        Handler().post(Runnable {
            Log.d("threads", "UI thread is working!")
            Log.d("threads", "result: " + future.get())
            Log.d("threads", "UI thread is blocked!")
        })
    }


}