package com.connor.moviecat.model.net

data class MovieUiResult(
    val id: Int,
    val posterPath: String,
    val mediaType: String,
    val title: String,
    val releaseOrFirstAirDate: String,
    val voteAverage: Double
)