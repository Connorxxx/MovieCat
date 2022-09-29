package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import com.connor.moviecat.Repository
import com.connor.moviecat.model.net.TrendingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainViewModel(private val repository: Repository) : ViewModel() {

    val loadList = ArrayList<TrendingResult>()

    var page = 1

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