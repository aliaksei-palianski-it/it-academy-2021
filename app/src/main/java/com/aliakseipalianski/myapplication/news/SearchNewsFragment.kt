package com.aliakseipalianski.myapplication.news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aliakseipalianski.myapplication.R
import kotlinx.android.synthetic.main.framgent_news_search.*

class SearchNewsFragment : Fragment(R.layout.framgent_news_search) {

    companion object {
        fun create() = SearchNewsFragment()
    }

    private val viewModel = viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.value.searchLiveData.observe(viewLifecycleOwner) {
            updateRecyclerView(viewModel.value.searchHistory as ArrayList<String>)
            // it.reduce { acc, any -> acc.toString() + any.toString() }.let { data ->
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        searchInput.doOnTextChanged { text, _, _, count ->
            text?.let {
                if (text.length > 2) {
                    viewModel.value.search(text)
                }
            }
        }
    }

    private fun updateRecyclerView(searchHistory: ArrayList<String>) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val userRecycle: RecyclerView? = view?.findViewById(R.id.history_recycler_view)
        userRecycle?.layoutManager = layoutManager
        val userAdapter = HistoryAdapter(searchHistory)
        userRecycle?.adapter = userAdapter
    }
}