package com.connor.moviecat.type

sealed interface DaoType<out T> {
    data class Delete(val id: Int): DaoType<Nothing>

    data class Insert<out T>(val movies: T): DaoType<T>
}

inline fun DaoType<*>.onDelete(delete: (Int) -> Unit) {
    if (this is DaoType.Delete) delete(id)
}

inline fun <T> DaoType<T>.onInsert(insert: (T) -> Unit) {
    if (this is DaoType.Insert) insert(movies)
}