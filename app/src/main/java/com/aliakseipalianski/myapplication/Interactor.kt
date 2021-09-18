package com.aliakseipalianski.myapplication

import com.aliakseipalianski.myapplication.news.SearchNewsRepository
import java.util.*


interface SearchUseCase {
}

interface RecentlySearchUseCase {
}

interface ClearRecentlySearchUseCase {
}

class SearchInteractor : SearchUseCase {
    fun execute(input: Any): Observable {
        return Observable();
    }
}

class NewsInteractor(
    private val repository: SearchNewsRepository
) : SearchUseCase,
    RecentlySearchUseCase,
    ClearRecentlySearchUseCase {

    fun executeSearch(input: Any): Observable {
        return Observable()
        //TODO return repository.addQueryToRecentlySearched(input)
    }

    fun executeRecentlyRecentlySearch(input: Any): Observable {
        return Observable()
        //TODO return repository.getAllRecentlySearched(input)
    }

    fun executeClearRecentlySearch(input: Any): Observable {
        return Observable()
        //TODO return repository.clearRecentlySearched(input)
    }
}