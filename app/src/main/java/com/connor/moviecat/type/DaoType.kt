package com.connor.moviecat.type

import com.connor.moviecat.model.room.MovieEntity

sealed class DaoType {
    data class Delete(val id: Int): DaoType()

    data class Insert(val movies: MovieEntity): DaoType()
}

inline fun DaoType.onDelete(delete: (Int) -> Unit) {
    if (this is DaoType.Delete) delete(id)
}

inline fun DaoType.onInsert(insert: (MovieEntity) -> Unit) {
    if (this is DaoType.Insert) insert(movies)
}