package com.connor.moviecat.model.net


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Trending(
    var page: Int,
    var results: List<TrendingResult>,
    @SerialName("total_pages")
    var totalPages: Int,
    @SerialName("total_results")
    var totalResults: Int
)

@Serializable
data class TrendingResult(
    var adult: Boolean,
    @SerialName("backdrop_path")
    var backdropPath: String,
    @SerialName("first_air_date")
    var firstAirDate: String? = null,
    @SerialName("genre_ids")
    var genreIds: List<Int>,
    var id: Int,
    @SerialName("media_type")
    var mediaType: String,
    var name: String? = null,
    @SerialName("origin_country")
    var originCountry: List<String>? = null,
    @SerialName("original_language")
    var originalLanguage: String,
    @SerialName("original_name")
    var originalName: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    @SerialName("overview")
    var overview: String,
    var popularity: Double,
    @SerialName("poster_path")
    var posterPath: String,
    @SerialName("release_date")
    var releaseDate: String? = null,
    var title: String? = null,
    var video: Boolean? = null,
    @SerialName("vote_average")
    var voteAverage: Double,
    @SerialName("vote_count")
    var voteCount: Int
)
