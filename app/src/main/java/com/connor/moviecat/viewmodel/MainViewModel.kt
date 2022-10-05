package com.connor.moviecat.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.connor.moviecat.model.Repository
import com.connor.moviecat.contract.Event
import com.connor.moviecat.ui.MovieFragment
import com.connor.moviecat.ui.TVShowFragment
import com.connor.moviecat.utlis.ModelMapper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _event = MutableSharedFlow<Event>()

    val event = _event.asSharedFlow()

    val titles = ArrayList<String>().apply {
        add("Movie")
        add("TV Show")
    }

    val fragments = ArrayList<Fragment>().apply {
        add(MovieFragment())
        add(TVShowFragment())
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    fun getPagingData(path: String) =
        repository.getPagingData(path)
            .map { pagingResult ->
                pagingResult.map { ModelMapper.toMovieUiResult(it) }
            }.cachedIn(viewModelScope)

    fun getSearchPagingData(path: String, query: String) =
        repository.getSearchPagingData(path, query)
            .map { pagingResult ->
                pagingResult.map { ModelMapper.toMovieUiResult(it) }
                    .filter { it.posterPath != null }
                    .filter { it.releaseOrFirstAirDate.length >= 4 }
            }
            .cachedIn(viewModelScope)

    override fun onCleared() {
        super.onCleared()
        Log.d("MovieFragment", "onCleared: ")
    }
}