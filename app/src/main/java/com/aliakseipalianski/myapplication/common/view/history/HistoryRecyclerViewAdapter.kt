package com.aliakseipalianski.myapplication.common.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliakseipalianski.myapplication.databinding.SearchQueryItemBinding


class HistoryRecyclerViewAdapter(
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {

    private val values = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchQueryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val query = values[position]
        holder.queryView.text = query
        with(holder.itemView) {
            tag = query
            setOnClickListener(onClickListener)
        }
    }

    fun updateValues(newValues: List<String>) {
        val historyDiffUtilCallback = HistoryDiffUtilCallback(values, newValues)
        val historyDiffResult = DiffUtil.calculateDiff(historyDiffUtilCallback, true)
        values.clear()
        values.addAll(newValues)
        historyDiffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: SearchQueryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val queryView: TextView = binding.query
    }
}