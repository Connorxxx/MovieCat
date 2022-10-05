package com.connor.moviecat.model.net

data class DetailUiModel(
    val title: String,
    val releaseOrFirstAirDate: String,
    val runtime: String?,
    val voteAverage: Double,
    val tagline: String,
    val overview: String
)