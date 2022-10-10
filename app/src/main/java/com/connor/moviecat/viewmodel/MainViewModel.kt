package com.connor.moviecat.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.connor.moviecat.model.Repository
import com.connor.moviecat.contract.Event
import com.connor.moviecat.model.net.MovieUiResult
import com.connor.moviecat.ui.MovieFragment
import com.connor.moviecat.ui.TVShowFragment
import com.connor.moviecat.utlis.ModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _event = MutableSharedFlow<Event>()
    private val _paging = Channel<Flow<PagingData<MovieUiResult>>>()

    val event = _event.asSharedFlow()

    @OptIn(FlowPreview::class)
    val paging = _paging.receiveAsFlow().flatMapMerge { it }.flowOn(Dispatchers.Default)

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    fun sendQuery(path: String, query: String? = null) {
        viewModelScope.launch {
            _paging.send(getSearchPagingData(path, query))
        }
    }

    fun getPagingData(path: String) =
        repository.getPagingData(path)
            .map { pagingResult ->
                pagingResult.map { ModelMapper.toMovieUiResult(it) }
            }.cachedIn(viewModelScope)

    private fun getSearchPagingData(path: String, query: String? = null) =
        repository.getPagingData(path, query)
            .map { pagingResult ->
                pagingResult.map { ModelMapper.toMovieUiResult(it) }
                    .filter { it.posterPath != null }
                    .filter { it.releaseOrFirstAirDate.length >= 4 }
            }.cachedIn(viewModelScope)


    val titles = ArrayList<String>().apply {
        add("Movie")
        add("TV Show")
    }

    val fragments = ArrayList<Fragment>().apply {
        add(MovieFragment())
        add(TVShowFragment())
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MovieFragment", "onCleared: ")
    }
}