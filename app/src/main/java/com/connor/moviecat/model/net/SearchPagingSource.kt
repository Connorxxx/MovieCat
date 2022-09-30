package com.connor.moviecat.model.net

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Exception

class SearchPagingSource(private val tmdbService: TMDBService, val query: String) : PagingSource<Int, MovieResult>()  {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {
            val page = params.key ?: 1
            val repoResponse = tmdbService.getSearch(query, page)
            val repoItems = repoResponse.results
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        TODO("Not yet implemented")
    }
}