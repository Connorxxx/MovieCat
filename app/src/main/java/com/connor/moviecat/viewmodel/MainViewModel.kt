package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.connor.moviecat.Repository
import kotlinx.coroutines.flow.map

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getPagingData(path: String, query: String? = null) =
        repository.getPagingData(path, query).map { pagingData ->
            pagingData.filter { it.posterPath != null }
                .filter { it.releaseDate?.length != 0 && it.firstAirDate?.length != 0 }
        }.cachedIn(viewModelScope)
}