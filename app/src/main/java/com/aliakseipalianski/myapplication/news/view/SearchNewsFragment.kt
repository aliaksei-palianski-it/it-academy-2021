package com.aliakseipalianski.myapplication.news.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.aliakseipalianski.myapplication.R
import com.aliakseipalianski.myapplication.news.view.LikeButton.DISABLED
import com.aliakseipalianski.myapplication.news.view.LikeButton.ENABLED
import com.aliakseipalianski.myapplication.news.view.adapter.HistoryRecyclerViewAdapter
import com.aliakseipalianski.myapplication.news.view.adapter.SearchNewsAdapter
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.framgent_news_search.*
import kotlinx.android.synthetic.main.item_search_news.*
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

        SearchDebugTools().makeSomething()

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
            historyRecycler.adapter?.let {
                val adapter = it as HistoryRecyclerViewAdapter
                adapter.updateValues(history)
                historyRecycler.scrollToPosition(0)
                if (adapter.itemCount > 0)
                    clearHistory.visibility = View.VISIBLE
                else
                    clearHistory.visibility = View.GONE
            }
        }

        searchInput.doOnTextChanged { text, _, _, count ->
            text?.let {
                if (text.length > 2 || text.isBlank()) {
                    viewModel.search(text.toString())
                }
            }
        }

        setupHistoryRecyclerView()

        clearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun setupHistoryRecyclerView() {
        historyRecycler.adapter = HistoryRecyclerViewAdapter { itemView ->
            val query = itemView.tag as String
            searchInput.setText(query)
        }
    }
}