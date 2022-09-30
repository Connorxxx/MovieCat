package com.connor.moviecat.model.net

import com.drake.net.Get
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class TMDBService {

    suspend fun getTrending(page: Int) = coroutineScope {
        Get<Trending>(ApiPath.TRENDING_ALL_WEEK) {
            param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            param("page", page)
        }.await()
    }

    suspend fun getMovie(page: Int) = coroutineScope {
        Get<Movie>(ApiPath.MOVIE) {
            param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            param("page", page)
        }.await()
    }

    suspend fun getTv(page: Int) = coroutineScope {
        Get<Movie>(ApiPath.TV) {
            param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            param("page", page)
        }.await()
    }

    suspend fun getSearch(query: String, page: Int) = coroutineScope {
        Get<Movie>(ApiPath.SEARCH_MULTI) {
            param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            param("query", query)
            param("page", page)
        }.await()
    }
}