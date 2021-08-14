package com.aliakseipalianski.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity(
    R.layout.activity_second
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.sendResultButton)?.setOnClickListener {
            setResult(RESULT_OK, Intent().putExtra(EXTRA_MESSAGE, getString(R.string.app_name)))
            finish()
        }

        /*
        TODO LEARN ViewBinding
        sendResultButton?.let {


        }
         */
    }
}