package com.aliakseipalianski.myapplication.news

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aliakseipalianski.myapplication.R
import kotlinx.android.synthetic.main.framgent_news_search.*

class SearchNewsFragment : Fragment(R.layout.framgent_news_search) {

    companion object {
        fun create() = SearchNewsFragment()
    }

    private val viewModel = viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.value.getRecentlySearched()
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
                if (text.length > 2) {
                    viewModel.value.search(text)
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