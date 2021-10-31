package com.aliakseipalianski.myapplication.news.view

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.aliakseipalianski.myapplication.R
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_news_detail.*

private const val ARG_ITEM = "item"
private const val ARG_TRANSTITION_NAME = "transtition_name"
private const val ARG_TRANSTITION_NAME_TEXT = "transtition_name_txt"
private const val ARG_TRANSTITION_NAME_TITLE = "transtition_name_title"

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private var item: NewsItem? = null
    private var tn: String? = null
    private var tn1: String? = null
    private var tn2: String? = null

    companion object {
        fun newInstance(
            item: NewsItem,
            transitionName: String,
            transitionName1: String,
            transitionName2: String
        ) =
            NewsDetailFragment().apply {
                arguments = bundleOf(
                    ARG_ITEM to item,
                    ARG_TRANSTITION_NAME to transitionName,
                    ARG_TRANSTITION_NAME_TEXT to transitionName1,
                    ARG_TRANSTITION_NAME_TITLE to transitionName2,
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getSerializable(ARG_ITEM) as? NewsItem
            tn = it.getString(ARG_TRANSTITION_NAME)
            tn1 = it.getString(ARG_TRANSTITION_NAME_TEXT)
            tn2 = it.getString(ARG_TRANSTITION_NAME_TITLE)
        }
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(
            context
        ).inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition
    }

    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        details_image.transitionName = tn
        details_text.transitionName = tn1
        title_text.transitionName = tn2

        prepareSharedElementTransition()

//        if (savedInstanceState == null) {
        postponeEnterTransition()
        //      }

        item?.let {
            Glide
                .with(details_image.context.applicationContext)
                .load(it.urlToImage)
                .fallback(R.mipmap.ic_launcher)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                }
                )

                .into(details_image)

            title_text.text = it.title
            details_text.text = it.description + it.description + it.description + it.description
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            onHomeAsUpSelected()
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun onHomeAsUpSelected(): Boolean {
        parentFragmentManager.popBackStack()
        return true
    }
}