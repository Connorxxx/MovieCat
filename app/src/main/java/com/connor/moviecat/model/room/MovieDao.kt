package com.connor.moviecat.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity): Long

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovie(id: Int): Int

    @Query("select * from movies ORDER BY title")
    fun loadAllMoves(): Flow<List<MovieEntity>>
}