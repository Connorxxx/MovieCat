package com.connor.moviecat.model.net

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.connor.moviecat.utlis.fire
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class MoviePagingSource(
    private val client: HttpClient,
    private val path: String,
    private val query: String? = null
) : PagingSource<Int, MovieResult>() {

    override suspend fun load(params: LoadParams<Int>) = fire {
        val page = params.key ?: 1
        val repoResponse = movie(path, page, query)

        LoadResult.Page(
            data = repoResponse.results,
            prevKey = if (page > 1) page - 1 else null,
            nextKey = if (page != repoResponse.totalPages && repoResponse.totalPages != 0) page + 1 else null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResult>) =
        state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    suspend fun movie(path: String, page: Int, query: String? = null) =
        client.get(path) {
            parameter("page", page)
            query?.let { parameter("query", it) }
        }.body<Movie>()

}