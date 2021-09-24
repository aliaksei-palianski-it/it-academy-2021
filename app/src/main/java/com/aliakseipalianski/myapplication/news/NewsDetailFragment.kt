package com.aliakseipalianski.myapplication.news

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
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

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        url?.let {
            with(webView) {
                webViewClient = object : WebViewClient() {

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        progressBar?.visibility = View.VISIBLE

                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (this@NewsDetailFragment.url == url)
                            progressBar?.visibility = View.GONE
                    }
                }

                settings.javaScriptEnabled = true
                loadUrl(it)
            }
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