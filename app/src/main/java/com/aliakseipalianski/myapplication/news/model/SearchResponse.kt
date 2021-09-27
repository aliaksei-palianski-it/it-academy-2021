package com.aliakseipalianski.myapplication.news.model

import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
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
) {
    fun toNewsItem(dateFormat: SimpleDateFormat) = NewsItem(
        UUID.nameUUIDFromBytes(toString().toByteArray()).toString(),
        author ?: "",
        title ?: "",
        description ?: "",
        urlToImage ?: "",
        if (publishedAt != null) dateFormat.format(publishedAt) else "",
        url ?: ""
    )
}

data class Source(
    val id: String?,
    val name: String,
)