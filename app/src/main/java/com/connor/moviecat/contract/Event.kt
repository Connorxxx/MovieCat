package com.connor.moviecat.contract

sealed class Event {
    object Scroll: Event()
}

inline fun Event.onScroll(block: () -> Unit) {
    if (this is Event.Scroll) block()
}
