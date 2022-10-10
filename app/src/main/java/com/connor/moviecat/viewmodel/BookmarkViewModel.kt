package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import com.connor.moviecat.model.DetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookmarkViewModel(private val repository: DetailRepository) : ViewModel() {

    private val _size = MutableStateFlow(0)

    val size = _size.asStateFlow()

    val getMovies = repository.getMovies()

    fun setSize(size: Int) {
        _size.value = size
    }

}