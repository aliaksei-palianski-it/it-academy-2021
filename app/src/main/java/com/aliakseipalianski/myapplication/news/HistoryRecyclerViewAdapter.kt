package com.aliakseipalianski.myapplication.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aliakseipalianski.myapplication.databinding.SearchQueryItemBinding

class HistoryRecyclerViewAdapter(
    private val values: List<String>,
    private val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {

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

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: SearchQueryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val queryView: TextView = binding.query
    }
}