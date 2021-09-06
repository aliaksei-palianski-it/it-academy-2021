package com.aliakseipalianski.myapplication.news

import android.os.Bundle
import android.view.View
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

        searchInput.doOnTextChanged { text, start, before, count ->
            //if (count > 0){
            //viewModel
            //}
        }
    }
}