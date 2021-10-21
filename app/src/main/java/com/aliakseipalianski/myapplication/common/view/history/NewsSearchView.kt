package com.aliakseipalianski.myapplication.common.view.history

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.aliakseipalianski.myapplication.R
import kotlinx.android.synthetic.main.layout_search.view.*
import kotlinx.coroutines.*

private const val START_POSITION = 0

class NewsSearchView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var mainScope: CoroutineScope? = null
    private var queryChangedJob: Job? = null

    init {
        init(
            context,
            attrs
        )
    }

    constructor (context: Context) : this(
        context,
        null,
        0,
        0
    )

    constructor (
        context: Context,
        attrs: AttributeSet?
    ) : this(
        context,
        attrs,
        0,
        0
    )

    constructor (
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(
        context,
        null,
        defStyleAttr,
        0
    )

    private fun init(
        context: Context,
        attrs: AttributeSet?,
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_search, this, true)
        setupHistoryAdapter()

        attrs?.let {
            val typedArray = resources.obtainAttributes(attrs, R.styleable.NewsSearchView)

            typedArray.getColor(
                R.styleable.NewsSearchView_cleanButtonColor,
                ContextCompat.getColor(context, R.color.black)
            ).let {
                clearHistory.setColorFilter(it)
            }

            typedArray.getResourceId(
                R.styleable.NewsSearchView_clearButtonSrc,
                R.drawable.ic_round_clear_24
            ).let {
                clearHistory.setImageResource(it)
            }

            typedArray.recycle()
        }
    }

    fun updateHistoryList(history: List<String>) {
        historyRecycler.adapter?.let {
            with(it as HistoryRecyclerViewAdapter) {
                this.updateValues(history)
                historyRecycler.scrollToPosition(START_POSITION)
                resolveClearHistoryButtonVisibility(this)
            }
        }
    }

    fun setClearHistoryListener(listener: () -> Unit) {
        clearHistory.setOnClickListener {
            listener.invoke()
        }
    }

    fun doOnSearchQueryChanged(listener: (searchQuery: String) -> Unit) {
        searchInput.doOnTextChanged { text, _, _, _ ->
            text?.let {
                onTextChanged(it, listener)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mainScope = MainScope()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (mainScope?.isActive == true) {
            mainScope?.cancel()
        }
    }

    private fun resolveClearHistoryButtonVisibility(adapter: HistoryRecyclerViewAdapter) {
        if (adapter.itemCount > 0)
            clearHistory.visibility = View.VISIBLE
        else
            clearHistory.visibility = View.GONE
    }

    private fun setupHistoryAdapter() {
        historyRecycler.adapter = HistoryRecyclerViewAdapter { itemView ->
            val query = itemView.tag as String
            searchInput.setText(query)
        }
    }

    private fun onTextChanged(text: CharSequence, listener: (searchQuery: String) -> Unit) {
        queryChangedJob?.cancel()
        queryChangedJob = mainScope?.launch {
            if (text.length > 2 || text.isBlank()) {
                delay(1000)
                listener.invoke(text.toString())
            }
        }
    }
}