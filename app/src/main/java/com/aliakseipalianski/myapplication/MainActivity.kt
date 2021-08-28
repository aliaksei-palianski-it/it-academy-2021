package com.aliakseipalianski.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

class MainActivity : AppCompatActivity(
    R.layout.activity_main
), MainActivityInteractionContract {

    private val viewModel = viewModels<SharedViewModel>()

    val temporaryValue: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(
                    R.id.container,
                    MainFragment.createInstance("Hello fragments!")
                ).commitNow()
        }

        viewModel.value.setTitle("Live data and View Model")
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

    override fun startRecyclerExample() {
        startActivity(Intent(this, RecyclerActivity::class.java))
    }

    override fun startListExample() {
        startActivity(Intent(this, ListActivity::class.java))
    }
}

interface MainActivityInteractionContract {
    fun startForResult()
    fun navigateToMainFragment()
    fun startRecyclerExample()
    fun startListExample()
}