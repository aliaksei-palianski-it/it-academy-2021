package com.aliakseipalianski.myapplication.news.view

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aliakseipalianski.myapplication.R
import com.aliakseipalianski.myapplication.news.view.adapter.SearchNewsAdapter
import com.aliakseipalianski.myapplication.news.viewModel.NewsItem
import com.aliakseipalianski.myapplication.news.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.framgent_news_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

object LikeButton {
    const val DISABLED = "disabled"
    const val ENABLED = "enabled"
}


class SearchNewsFragment : Fragment(R.layout.framgent_news_search) {

    companion object {
        var position: Int = 0;
        fun create() = SearchNewsFragment()
    }


    private val viewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)
        postponeEnterTransition()

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

        newsRecycler.adapter = SearchNewsAdapter(::onLoaded) {
            val item = it.tag

            if (item is NewsItem) {
                val posterView: View = it.findViewById(R.id.itemPoster)
                val title: View = it.findViewById(R.id.itemTitle)
                val textDesc: View = it.findViewById(R.id.itemDescription)
                val fragment = NewsDetailFragment.newInstance(
                    item,
                    posterView.transitionName,
                    textDesc.transitionName,
                    title.transitionName,
                )
                (exitTransition as TransitionSet).excludeTarget(it, true)

                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack("SearchNewsFragment")
                    .addSharedElement(posterView, posterView.transitionName)
                    .addSharedElement(textDesc, textDesc.transitionName)
                    .addSharedElement(title, title.transitionName)
                    .replace(
                        R.id.container, fragment, NewsDetailFragment::class.java
                            .simpleName
                    )
                    .commit()
            }
        }

        viewModel.historyLiveData.observe(viewLifecycleOwner) { history ->
            newsSearchView.updateHistoryList(history)
        }

        newsSearchView.doOnSearchQueryChanged(viewModel::search)
        newsSearchView.setClearHistoryListener(viewModel::clearHistory)
    }

    private fun onLoaded() {
        startPostponedEnterTransition()
    }
}