package com.connor.moviecat.model.net

import com.drake.net.Get
import com.drake.net.request.UrlRequest
import kotlinx.coroutines.coroutineScope

class TMDBService {

    suspend fun getTrending(page: Int, path: String, block: UrlRequest.() -> Unit) = coroutineScope {
        Get<Movie>(path) {
            param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
            param("page", page)
            block()
        }.await()
    }
}