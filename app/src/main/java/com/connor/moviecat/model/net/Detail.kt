package com.connor.moviecat.model.net


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Detail(
    var adult: Boolean? = null,
    @SerialName("backdrop_path")
    var backdropPath: String? = null,
    var budget: Int? = null,
    var homepage: String? = null,
    var id: Int? = null,
    @SerialName("imdb_id")
    var imdbId: String? = null,
    @SerialName("original_language")
    var originalLanguage: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    var overview: String? = null,
    var popularity: Double? = null,
    @SerialName("poster_path")
    var posterPath: String? = "",
    @SerialName("release_date")
    var releaseDate: String? = null,
    @SerialName("first_air_date")
    var firstAirDate: String? = null,
    var status: String? = "",
    var tagline: String? = "",
    var title: String? = null,
    var name: String? = null,
    var video: Boolean? = false,
    var runtime: Int? = null,
    @SerialName("number_of_seasons")
    var NumberOfSeasons: Int? = null,
    @SerialName("vote_average")
    var voteAverage: Double? = null,
    @SerialName("vote_count")
    var voteCount: Int? = 0
)