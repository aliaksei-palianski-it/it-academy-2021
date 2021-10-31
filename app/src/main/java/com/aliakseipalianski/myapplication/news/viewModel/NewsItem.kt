package com.aliakseipalianski.myapplication.news.viewModel

import java.io.Serializable

data class NewsItem(
    val id: String,
    val author: String,
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val url: String
) : Serializable