package com.github.edasich.base.ui

import androidx.lifecycle.Observer

/**
 * An [Observer] for [SingleEvent]s, simplifying the pattern of checking if the [SingleEvent]'s content has
 * already been handled.
 * [onEventUnhandledContent] is *only* called if the [SingleEvent]'s contents has not been handled.
 * See more: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(singleEvent: SingleEvent<T>?) {
        singleEvent?.getIfEventNotHandled()?.let(onEventUnhandledContent)
    }
}