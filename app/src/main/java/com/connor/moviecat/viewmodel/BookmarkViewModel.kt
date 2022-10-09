package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import com.connor.moviecat.model.DetailRepository

class BookmarkViewModel(private val repository: DetailRepository) : ViewModel() {

    val getMovies = repository.getMovies()

}