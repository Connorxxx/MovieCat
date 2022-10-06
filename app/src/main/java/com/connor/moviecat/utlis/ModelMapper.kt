package com.connor.moviecat.utlis

import com.connor.moviecat.model.net.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object ModelMapper {

    fun toDetailUiModel(detail: Detail) = DetailUiModel(
        id = detail.id ?: 1,
        title = getTitle(detail.title, detail.name),
        releaseOrFirstAirDate = getFormattedDate(detail.releaseDate, detail.firstAirDate),
        runtime = getRuntimeOrSeasons(detail.runtime, detail.NumberOfSeasons).toString(),  //detail.runtime?.let { getRuntime(it) },
        voteAverage = text(detail.voteAverage ?: 0.0),
        tagline = detail.tagline ?: "",
        overview = detail.overview ?: "",
        homepage = detail.homepage,
        imdbId = detail.imdbId ?: ""
    )

    private fun text(double: Double) = BigDecimal(double).setScale(1,BigDecimal.ROUND_HALF_UP).toDouble()
        //DecimalFormat("#.00").format(double).toDouble()

    fun toMovieUiResult(result: MovieResult) = MovieUiResult(
        id = result.id,
        posterPath = result.posterPath,
        mediaType = result.mediaType ?: "movie",
        title = getTitle(result.title, result.name),
        releaseOrFirstAirDate = getDate(result.releaseDate, result.firstAirDate),
        voteAverage = result.voteAverage
    )

    private fun getTitle(title: String?, name: String?) = title ?: name.orEmpty()

    private fun getFormattedDate(releaseDate: String?, firstAirDate: String?) = releaseDate?.takeIf {
        it.isNotBlank()
    }?.formatDate() ?: firstAirDate?.takeIf {
        it.isNotBlank()
    }?.formatDate().orEmpty()

    private fun getDate(releaseDate: String?, firstAirDate: String?) = releaseDate?.takeIf {
        it.isNotBlank()
    } ?: firstAirDate?.takeIf {
        it.isNotBlank()
    } ?: ""

    private fun getRuntimeOrSeasons(runtime: Int?, seasons: Int?) =
        if (runtime != null) {
        getRuntime(runtime)
    } else if (seasons != null) "Seasons $seasons"  else ""

    private fun getRuntime(runtime: Int) = "${runtime.toHours()} h ${runtime % 60} min"

    private fun String.formatDate(stringPattern: String = "yyyy-MM-dd",
                                  pattern: String = "MMM d, yyyy"): String {
        val date = SimpleDateFormat(stringPattern, Locale.ENGLISH).parse(this)
        return date?.let {
            SimpleDateFormat(pattern, Locale.ENGLISH).format(it)
        }.orEmpty()
    }
}