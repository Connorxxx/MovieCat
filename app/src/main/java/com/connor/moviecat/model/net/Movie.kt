package com.connor.moviecat.model.net


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    var page: Int,
    var results: List<MovieResult>,
    @SerialName("total_pages")
    var totalPages: Int,
    @SerialName("total_results")
    var totalResults: Int
)
    @Serializable
    data class MovieResult(
        var adult: Boolean,
        @SerialName("backdrop_path")
        var backdropPath: String,
        @SerialName("genre_ids")
        var genreIds: List<Int>,
        var id: Int,
        @SerialName("original_language")
        var originalLanguage: String,
        @SerialName("original_title")
        var originalTitle: String,
        var overview: String,
        var popularity: Double,
        @SerialName("poster_path")
        var posterPath: String,
        @SerialName("release_date")
        var releaseDate: String,
        var title: String,
        var video: Boolean,
        @SerialName("vote_average")
        var voteAverage: Double,
        @SerialName("vote_count")
        var voteCount: Int
    )
