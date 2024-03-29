package com.connor.moviecat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.moviecat.model.DetailRepository
import com.connor.moviecat.model.room.MovieEntity
import com.connor.moviecat.type.DaoType
import com.connor.moviecat.utlis.ModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {

    private val _check = MutableStateFlow(false)
    val check = _check.asStateFlow()

    private val _dao = Channel<DaoType<MovieEntity>>()
    val dao = _dao.receiveAsFlow()

    val getMovies = repository.getMovies()

    fun setCheck(check: Boolean) {
        _check.value = check
    }

    fun sendDaoType(daoType: DaoType<MovieEntity>) {
        viewModelScope.launch {
            _dao.send(daoType)
        }
    }

    suspend fun insertMovie(movies: MovieEntity) {
        repository.insertMovie(movies)
    }

    suspend fun deleteMovie(id: Int) {
        repository.deleteMovie(id)
    }

    fun detail(type: String, id: String) = flow {
        emit(repository.detail(type, id))
    }.map {
        ModelMapper.toDetailUiModel(it)
    }.flowOn(Dispatchers.IO)
}