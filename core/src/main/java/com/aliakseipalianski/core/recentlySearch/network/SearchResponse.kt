package com.aliakseipalianski.core.recentlySearch.network

import com.google.gson.annotations.SerializedName
import java.util.*

data class SearchResponse(
    val status: String,
    val totalResults: Int,
    @SerializedName("articles") val articleList: List<SearchItem>,
)

data class SearchItem(
    val source: Source,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: Date?,
    val content: String,
)

data class Source(
    val id: String?,
    val name: String,
)