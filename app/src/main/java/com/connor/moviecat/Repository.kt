package com.connor.moviecat

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.connor.moviecat.model.net.*
import com.drake.logcat.LogCat
import com.drake.net.Get
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class Repository(private val tmdbService: TMDBService) {

    fun getPagingData() = Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { RepoPagingSource(tmdbService) }
        ).flow

    fun getMoviePagingData() = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { MoviePagingSource(tmdbService) }
    ).flow

    fun getTVPagingData() = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { TVPagingSource(tmdbService) }
    ).flow

}