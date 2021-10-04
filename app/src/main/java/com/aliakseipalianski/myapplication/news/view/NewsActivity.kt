package com.aliakseipalianski.myapplication.news.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aliakseipalianski.myapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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