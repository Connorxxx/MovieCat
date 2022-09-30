package com.connor.moviecat.model.net


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    var page: Int = 1,
    var results: List<MovieResult> = listOf(),
    @SerialName("total_pages")
    var totalPages: Int = 1,
    @SerialName("total_results")
    var totalResults: Int = 1
)

@Serializable
data class MovieResult(
    var adult: Boolean = false,
    @SerialName("backdrop_path")
    var backdropPath: String = "",
    @SerialName("genre_ids")
    var genreIds: List<Int> = listOf(),
    var id: Int = 1,
    @SerialName("original_language")
    var originalLanguage: String = "",
    @SerialName("original_title")
    var originalTitle: String = "",
    var overview: String = "",
    var popularity: Double = 1.1,
    @SerialName("poster_path")
    var posterPath: String? = null,
    @SerialName("release_date")
    var releaseDate: String? = null,
    @SerialName("first_air_date")
    var firstAirDate: String? = null,
    var title: String? = null,
    var name: String? =null,
    var video: Boolean = false,
    @SerialName("vote_average")
    var voteAverage: Double = 0.0,
    @SerialName("vote_count")
    var voteCount: Int = 0
)
