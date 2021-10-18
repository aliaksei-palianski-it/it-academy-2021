package com.aliakseipalianski.myapplication.news.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aliakseipalianski.myapplication.R
import com.aliakseipalianski.myapplication.news.view.adapter.SearchNewsAdapter
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.framgent_news_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

object LikeButton {
    const val DISABLED = "disabled"
    const val ENABLED = "enabled"
}

class SearchNewsFragment : Fragment(R.layout.framgent_news_search) {

    companion object {
        fun create() = SearchNewsFragment()
    }

    private val viewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getRecentlySearched()
            viewModel.search("")
        }

        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            (newsRecycler.adapter as? SearchNewsAdapter)?.submitList(it)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            Log.d("error", it)
        }

        newsRecycler.adapter = SearchNewsAdapter {
            val url = it.tag
            if (url is String && url.isNotEmpty()) {
                parentFragmentManager.beginTransaction()
                    .addToBackStack("SearchNewsFragment")
                    .replace(R.id.container, NewsDetailFragment.newInstance(url))
                    .commit()
            }
        }

        viewModel.historyLiveData.observe(viewLifecycleOwner) { history ->
            newsSearchView.updateHistoryList(history)
        }

        newsSearchView.doOnSearchQueryChanged(viewModel::search)
        newsSearchView.setClearHistoryListener(viewModel::clearHistory)
    }
}