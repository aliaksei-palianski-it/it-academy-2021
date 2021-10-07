package com.aliakseipalianski.myapplication.common

import com.aliakseipalianski.core.recentlySearch.network.SearchItem
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import java.text.SimpleDateFormat
import java.util.*

fun SearchItem.toNewsItem(dateFormat: SimpleDateFormat) = NewsItem(
    UUID.nameUUIDFromBytes(toString().toByteArray()).toString(),
    author ?: "",
    title ?: "",
    description ?: "",
    urlToImage ?: "",
    if (publishedAt != null) dateFormat.format(publishedAt) else "",
    url ?: ""
)