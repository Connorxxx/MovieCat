package com.connor.moviecat.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [MovieEntity::class])
abstract class MovieDataBase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao

    companion object {
        private var instance: MovieDataBase? = null

        @Synchronized
        fun getDataBase(context: Context): MovieDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
            MovieDataBase::class.java, "moves_database").build().apply {
                instance = this
            }
        }
    }
}