package com.aliakseipalianski.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

class MainActivity : AppCompatActivity(
    R.layout.activity_main
), MainActivityInteractionContract {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container,
                    MainFragment()
                ).commit()
        }
    }

    private val startForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra(EXTRA_MESSAGE)?.let { message ->
                    when (val fragment = supportFragmentManager.findFragmentById(R.id.container)) {
                        is MainFragment -> fragment.handleResult(message)
                        else -> Log.d(
                            MainActivity::class.java.name,
                            "cannot find fragment"
                        )
                    }
                }
            }
        }

    override fun startForResult() {
        startForResult.launch(
            Intent(
                this,
                SecondActivity::class.java
            )
        )
    }

    override fun navigateToMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment())
            .commit()
    }
}

interface MainActivityInteractionContract {
    fun startForResult()
    fun navigateToMainFragment()
}