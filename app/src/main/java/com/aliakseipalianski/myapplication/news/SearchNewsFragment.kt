package com.aliakseipalianski.myapplication.news

import android.os.Bundle
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

    private val history = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.value.searchLiveData.observe(viewLifecycleOwner) {
            //TODO set data to recycler view
            // it.reduce { acc, any -> acc.toString() + any.toString() }.let { data ->
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            //  }
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.value.historyLiveData.observe(viewLifecycleOwner) {
            for (query in it) {
                if (!history.contains(query)) {
                    history.add(query)
                    historyRecycler.adapter?.notifyItemInserted(history.size - 1)
                    clearHistory.visibility = View.VISIBLE
                }
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

        clearHistory.setOnClickListener(onClearClicked)
    }

    private val onClearClicked = View.OnClickListener {
        clearHistory.visibility = View.GONE
        viewModel.value.clearHistory()
        val size = history.size
        history.clear()
        historyRecycler.adapter?.notifyItemRangeRemoved(0, size)
    }

    private fun setupHistoryRecyclerView() {
        historyRecycler.adapter = HistoryRecyclerViewAdapter(history) { itemView ->
            val query = itemView.tag as String
            searchInput.setText(query)
        }
    }
}