package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.connor.moviecat.Repository
import com.connor.moviecat.model.net.TrendingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainViewModel(private val repository: Repository) : ViewModel() {

    val loadList = ArrayList<TrendingResult>()

    var page = 1

    fun getPagingData() = repository.getPagingData().cachedIn(viewModelScope)

    fun getMoviePagingData() = repository.getMoviePagingData().cachedIn(viewModelScope)

    fun getTrending(page: Int) = repository.getTrending(page)
        .filterNotNull()
        .onEach { loadList.add(it) }
        .map { loadList }
        .flowOn(Dispatchers.IO)
//        .shareIn(
//            viewModelScope,
//            replay = 1,
//            started = SharingStarted.WhileSubscribed(5000)
//        )
}