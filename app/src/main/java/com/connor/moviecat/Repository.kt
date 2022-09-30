package com.connor.moviecat

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.connor.moviecat.model.net.*

class Repository(private val tmdbService: TMDBService) {

    fun getPagingData(path: String, query: String? = null) = Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { RepoPagingSource(tmdbService, path, query) }
        ).flow
}