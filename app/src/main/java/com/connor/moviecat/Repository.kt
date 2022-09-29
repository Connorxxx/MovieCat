package com.connor.moviecat

import com.connor.moviecat.model.net.ApiPath
import com.connor.moviecat.model.net.Trending
import com.drake.logcat.LogCat
import com.drake.net.Get
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class Repository {

    fun getTrending(page: Int) = flow {
        val trending = coroutineScope {
            Get<Trending>(ApiPath.TRENDING_ALL_WEEK) {
                param(ApiPath.API_KEY, ApiPath.API_KEY_VALUE)
                param("page", page)
            }.await()
        }
        trending.results!!.forEach { emit(it) }
    }
//        .catch {
//        LogCat.e("Net Error")
//    }
}