package com.aliakseipalianski.myapplication.news.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aliakseipalianski.myapplication.R
import com.aliakseipalianski.myapplication.news.view.LikeButton
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class SearchNewsAdapter(
    private val onLoaded: () -> Unit,
    private val onClickListener: View.OnClickListener
) :
    ListAdapter<NewsItem, SearchNewsAdapter.SearchNewsItemViewHolder>(SearchNewsItemDiff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = SearchNewsItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_search_news,
            parent,
            false
        ),
        onLoaded
    )

    override fun onBindViewHolder(
        holder: SearchNewsItemViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onClickListener, position)
    }

    class SearchNewsItemViewHolder(view: View, val onLoaded: () -> Unit) :
        RecyclerView.ViewHolder(view) {

        private val poster: ImageView = view.findViewById(R.id.itemPoster)
        private val title: TextView = view.findViewById(R.id.itemTitle)
        private val description: TextView = view.findViewById(R.id.itemDescription)
        private val author: TextView = view.findViewById(R.id.itemAuthor)
        private val date: TextView = view.findViewById(R.id.itemDate)
        private val likeButton: ImageButton = view.findViewById(R.id.likeButton)

        fun bind(item: NewsItem, onClickListener: View.OnClickListener, position: Int) {
            poster.transitionName = "timage$position"
            description.transitionName = "ttext$position"
            title.transitionName = "ttitle$position"
            author.text = item.author
            description.text = item.description
            date.text = item.publishedAt
            title.text = item.title
            likeButton.setOnClickListener {
                when (likeButton.tag) {
                    LikeButton.DISABLED -> {
                        likeButton.setColorFilter(
                            ContextCompat.getColor(
                                likeButton.context,
                                android.R.color.holo_orange_light
                            )
                        )
                        likeButton.tag = LikeButton.ENABLED
                    }
                    LikeButton.ENABLED -> {
                        likeButton.setColorFilter(
                            ContextCompat.getColor(
                                likeButton.context,
                                android.R.color.darker_gray
                            )
                        )
                        likeButton.tag = LikeButton.DISABLED
                    }
                }
            }

            Glide
                .with(itemView.context.applicationContext)
                .load(item.urlToImage)
                .fallback(R.mipmap.ic_launcher)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        onLoaded.invoke()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        onLoaded.invoke()
                        return false
                    }

                })
                .into(poster)

            with(itemView) {
                tag = item

                setOnClickListener(onClickListener)
            }
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