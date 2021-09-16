package com.aliakseipalianski.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(
    R.layout.activity_main
) {
    private lateinit var controller: MainController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = MainModel()
        controller = MainController(model)
        model.changedState.observe(this) {
            // update UI
        }

        textView.doAfterTextChanged(controller::handleSearchTextChange)

        controller.handleLink(intent)
        registerReceiver(controller, MainController.OtherActivityBroadcastIntentFilter)
        text.setOnClickListener(controller)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            controller.handleLink(it)
        }
    }
}