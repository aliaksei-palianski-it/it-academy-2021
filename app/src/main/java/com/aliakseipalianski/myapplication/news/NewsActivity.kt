package com.aliakseipalianski.myapplication.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aliakseipalianski.myapplication.R

class NewsActivity : AppCompatActivity(R.layout.activity_news) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchNewsFragment.create())
                .commit()
        }
    }
}