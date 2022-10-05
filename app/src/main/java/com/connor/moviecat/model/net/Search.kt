package com.connor.moviecat.model.net


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    var page: Int? = 1,
    var results: List<MultiResult> = listOf(),
    @SerialName("total_pages")
    var totalPages: Int? = 1,
    @SerialName("total_results")
    var totalResults: Int? = 1
)

@Serializable
data class MultiResult(
    var adult: Boolean? = null,
    @SerialName("backdrop_path")
    var backdropPath: String? = null,
    @SerialName("first_air_date")
    var firstAirDate: String? = null,
    @SerialName("genre_ids")
    var genreIds: List<Int?>? = listOf(),
    var id: Int? = null,
    @SerialName("media_type")
    var mediaType: String? = null,
    var name: String? = null,
    @SerialName("origin_country")
    var originCountry: List<String?>? = null,
    @SerialName("original_language")
    var originalLanguage: String? = null,
    @SerialName("original_name")
    var originalName: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    var overview: String? = null,
    var popularity: Double? = null,
    @SerialName("poster_path")
    var posterPath: String? = null,
    @SerialName("release_date")
    var releaseDate: String? = null,
    var title: String? = null,
    var video: Boolean? = null,
    @SerialName("vote_average")
    var voteAverage: Double? = null,
    @SerialName("vote_count")
    var voteCount: Int? = null
)