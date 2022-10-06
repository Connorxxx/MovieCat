package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.moviecat.model.DetailRepository
import com.connor.moviecat.model.room.MovieDao
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.utlis.ModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {

    var inCheck = false

    private val _insert = Channel<Long>()
    val insert = _insert.receiveAsFlow()

    private val _delete = Channel<Int>()
    val delete = _delete.receiveAsFlow()

    val getMovies = repository.getMovies()

    fun insertMovie(movies: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _insert.send(repository.insertMovie(movies))
        }
    }

    fun deleteMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _delete.send(repository.deleteMovie(id))
        }
    }


    fun detail(type: String, id: String) = flow {
        emit(repository.detail(type, id))
    }.map {
        ModelMapper.toDetailUiModel(it)
    }.flowOn(Dispatchers.IO)
}