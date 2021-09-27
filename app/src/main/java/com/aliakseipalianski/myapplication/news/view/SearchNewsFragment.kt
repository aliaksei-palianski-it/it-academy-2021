package com.aliakseipalianski.myapplication.news.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aliakseipalianski.myapplication.R
import com.aliakseipalianski.myapplication.news.view.adapter.HistoryRecyclerViewAdapter
import com.aliakseipalianski.myapplication.news.view.adapter.SearchNewsAdapter
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.framgent_news_search.*

class SearchNewsFragment : Fragment(R.layout.framgent_news_search) {

    companion object {
        fun create() = SearchNewsFragment()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var viewModel = viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.value.getRecentlySearched()
            viewModel.value.search("")
        }

        viewModel.value.searchLiveData.observe(viewLifecycleOwner) {
            (newsRecycler.adapter as? SearchNewsAdapter)?.submitList(it)
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            Log.d("error", it)
        }

        newsRecycler.adapter = SearchNewsAdapter()

        viewModel.value.historyLiveData.observe(viewLifecycleOwner) { history ->
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
                    viewModel.value.search(text.toString())
                }
            }
        }

        setupHistoryRecyclerView()

        clearHistory.setOnClickListener {
            viewModel.value.clearHistory()
        }
    }

    private fun setupHistoryRecyclerView() {
        historyRecycler.adapter = HistoryRecyclerViewAdapter { itemView ->
            val query = itemView.tag as String
            searchInput.setText(query)
        }
    }
}