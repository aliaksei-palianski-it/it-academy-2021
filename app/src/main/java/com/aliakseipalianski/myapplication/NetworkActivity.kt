package com.aliakseipalianski.myapplication

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class NetworkActivity : AppCompatActivity(R.layout.network_activity) {

    private val viewModel = viewModels<NetworkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.value.networkLiveData.observe(this) {
            Log.d(
                "new_thread",
                "is main (main)" + (Looper.myLooper() == Looper.getMainLooper()).toString()
            )

            Log.d(
                "new_thread",
                it
            )
        }

        viewModel.value.scheduledLoadDataBackground()
     //   viewModel.value.singleThreadExecutorLoadDataBackground()
        //viewModel.value.fixedThreadPoolLoadDataBackground()

        /*
        Log.d(
            "new_thread",
            "is main (main)" + (Looper.myLooper() == Looper.getMainLooper()).toString()
        )
        functionOnlyInBackground()
        val runnable = Runnable {
            // for(i in 1..10)
            synchronized(this) {
                // repeat(100) {
                //  Log.d("new_thread", "thread one $it")

                Log.d(
                    "new_thread",
                    "is main (background)" + (Looper.myLooper() == Looper.getMainLooper()).toString()
                )


                Handler(Looper.getMainLooper()).post {
                    Log.d(
                        "new_thread",
                        "is main (handler)" + (Looper.myLooper() == Looper.getMainLooper()).toString()
                    )
                }
            }
        }

        val runnableTwo = Runnable {
            synchronized(this) {
                repeat(100) {
                    Log.d("new_thread", "thread two $it")
                }
            }
        }

        Thread(runnable).start()
        Thread(runnableTwo).start()
        */
    }
/*
    private fun functionOnlyInBackground() {
        if (Looper.myLooper() == Looper.getMainLooper() && BuildConfig.DEBUG) {
            throw Exception("Must be running only in background thread")
        }
    }
    */
}