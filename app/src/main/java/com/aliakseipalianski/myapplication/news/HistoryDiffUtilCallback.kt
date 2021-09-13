package com.aliakseipalianski.myapplication.news

import androidx.recyclerview.widget.DiffUtil

class HistoryDiffUtilCallback(
    private val oldList: List<String>,
    private val newList: List<String>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuery = oldList[oldItemPosition]
        val newQuery = newList[newItemPosition]
        return oldQuery == newQuery
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuery = oldList[oldItemPosition]
        val newQuery = newList[newItemPosition]
        return oldQuery == newQuery
    }
}