package com.connor.moviecat.model

import com.connor.moviecat.model.net.Detail
import com.connor.moviecat.model.room.MovieDao
import com.connor.moviecat.model.room.MovieEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class DetailRepository(
    private val client: HttpClient,
    private val movieDao: MovieDao
) {

    suspend fun detail(type: String, id: String) = client.get {
        url { path(type, id) }
    }.body<Detail>()

    suspend fun insertMovie(movie: MovieEntity) = movieDao.insertMovie(movie)

    suspend fun deleteMovie(id: Int) = movieDao.deleteMovie(id)

    fun getMovies() = movieDao.loadAllMoves()
}