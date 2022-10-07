package com.connor.moviecat.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.connor.moviecat.model.net.*
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class Repository(private val client: HttpClient) {

    fun getPagingData(path: String, query: String? = null) = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { MoviePagingSource(client, path, query) }
    ).flow.flowOn(Dispatchers.IO)
}
