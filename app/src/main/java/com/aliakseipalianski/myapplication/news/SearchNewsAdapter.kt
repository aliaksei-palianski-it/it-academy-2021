package com.aliakseipalianski.myapplication.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aliakseipalianski.myapplication.R
import com.bumptech.glide.Glide

class SearchNewsAdapter :
    ListAdapter<NewsItem, SearchNewsAdapter.SearchNewsItemViewHolder>(SearchNewsItemDiff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = SearchNewsItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_search_news,
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: SearchNewsItemViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class SearchNewsItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val poster: ImageView = view.findViewById(R.id.itemPoster)
        private val title: TextView = view.findViewById(R.id.itemTitle)
        private val description: TextView = view.findViewById(R.id.itemDescription)
        private val author: TextView = view.findViewById(R.id.itemAuthor)
        private val date: TextView = view.findViewById(R.id.itemDate)

        fun bind(item: NewsItem) {
            author.text = item.author
            description.text = item.description
            date.text = item.publishedAt
            title.text = item.title

            Glide
                .with(itemView.context.applicationContext)
                .load(item.urlToImage)
                .fallback(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(poster)
        }
    }
}

object SearchNewsItemDiff : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem == newItem
    }
}