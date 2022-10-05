package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import com.connor.moviecat.model.DetailRepository
import com.connor.moviecat.utlis.ModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {

    fun detail(type: String, id: String) = flow {
        emit(repository.detail(type, id))
    }.map {
        ModelMapper.toDetailUiModel(it)
    }.flowOn(Dispatchers.IO)
}