package com.aliakseipalianski.myapplication.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import com.aliakseipalianski.myapplication.R
import kotlinx.android.synthetic.main.fragment_news_detail.*

private const val ARG_URL = "url"

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private var url: String? = null

    companion object {
        fun newInstance(url: String) =
            NewsDetailFragment().apply {
                arguments = bundleOf(ARG_URL to url)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        url?.let {
            with(webView) {
                settings.javaScriptEnabled = true
                loadUrl(it)
            }
        }
    }
}