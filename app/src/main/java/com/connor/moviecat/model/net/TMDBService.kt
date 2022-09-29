package com.connor.moviecat.model.net

import com.drake.net.Get
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class TMDBService {
    suspend fun getTrending(page: Int)  = coroutineScope {
            Get<Trending>(ApiPath.TRENDING_ALL_WEEK) {
                param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
                param("page", page)
            }.await()
        }
    }