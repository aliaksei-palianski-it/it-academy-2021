package com.aliakseipalianski.myapplication.news.viewModel

data class NewsItem(
    val id: String,
    val author: String,
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val url: String
)