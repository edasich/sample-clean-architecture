package com.github.edasich.base.ui

import androidx.lifecycle.MutableLiveData

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * See more: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
open class SingleEvent<out T>(private val event: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the event and prevents its use again.
     */
    fun getIfEventNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            event
        }
    }

    /**
     * Returns the event, even if it's already been handled.
     */
    fun peekEvent(): T = event
}


fun <T> MutableLiveData<SingleEvent<T>>.sendEvent(data: T) {
    value = SingleEvent(data)
}

fun <T> MutableLiveData<SingleEvent<T>>.postEvent(data: T) {
    postValue(SingleEvent(data))
}