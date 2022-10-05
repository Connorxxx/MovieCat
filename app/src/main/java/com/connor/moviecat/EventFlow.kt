package com.connor.moviecat

import com.connor.moviecat.contract.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object EventFlow {

    val job = Job()
    private val scope = CoroutineScope(job)

    private val mutableEvent = MutableSharedFlow<Event>(replay = 1)

    val event = mutableEvent.asSharedFlow()

    fun sendEvent(event: Event) {
        scope.launch {
            mutableEvent.emit(event)
        }
    }
}