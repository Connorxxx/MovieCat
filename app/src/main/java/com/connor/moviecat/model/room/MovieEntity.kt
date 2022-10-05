package com.connor.moviecat.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val poster: String,
    val title: String,
    val releasedYear: String,
    val voteAverage: Double,
)