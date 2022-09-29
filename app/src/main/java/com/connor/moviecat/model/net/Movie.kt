package com.connor.moviecat.model.net


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    var page: Int = 0,
    var results: List<MovieResult> = listOf(),
    @SerialName("total_pages")
    var totalPages: Int = 0,
    @SerialName("total_results")
    var totalResults: Int = 0
)

@Serializable
data class MovieResult(
    var adult: Boolean = false,
    @SerialName("backdrop_path")
    var backdropPath: String = "",
    @SerialName("genre_ids")
    var genreIds: List<Int> = listOf(),
    var id: Int = 0,
    @SerialName("original_language")
    var originalLanguage: String = "",
    @SerialName("original_title")
    var originalTitle: String = "",
    var overview: String = "",
    var popularity: Double = 0.0,
    @SerialName("poster_path")
    var posterPath: String = "",
    @SerialName("release_date")
    var releaseDate: String = "",
    var title: String = "",
    var video: Boolean = false,
    @SerialName("vote_average")
    var voteAverage: Double = 0.0,
    @SerialName("vote_count")
    var voteCount: Int = 0
)
