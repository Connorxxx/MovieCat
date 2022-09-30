package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.connor.moviecat.Repository
import com.connor.moviecat.model.net.TrendingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getPagingData() = repository.getPagingData().cachedIn(viewModelScope)

    fun getMoviePagingData() = repository.getMoviePagingData().cachedIn(viewModelScope)

    fun getTVPagingData() = repository.getTVPagingData().cachedIn(viewModelScope)
}