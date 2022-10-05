package com.connor.moviecat.model

import com.connor.moviecat.model.net.Detail
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class DetailRepository(private val client: HttpClient) {

    suspend fun detail(type: String, id: String) = client.get {
        url { path(type, id) }
    }.body<Detail>()
}