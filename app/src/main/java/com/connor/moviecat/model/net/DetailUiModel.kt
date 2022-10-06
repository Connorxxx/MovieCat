package com.connor.moviecat.model.net

data class DetailUiModel(
    val id: Int,
    val title: String,
    val releaseOrFirstAirDate: String,
    val runtime: String?,
    val voteAverage: Double,
    val tagline: String,
    val overview: String,
    val homepage: String,
    val imdbId: String
)