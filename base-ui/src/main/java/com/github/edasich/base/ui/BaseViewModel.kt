package com.github.edasich.base.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<REQUEST, STATE, EVENT, VIEW> : ViewModel() {

    protected open val _state: MutableLiveData<VIEW> = MutableLiveData()
    val state: LiveData<VIEW>
        get() = _state

    private val _event = MutableLiveData<SingleEvent<EVENT>>()
    val event: LiveData<SingleEvent<EVENT>>
        get() = _event

    abstract fun processRequest(request: REQUEST)

    protected abstract fun setState(state: STATE)

    protected fun sendEvent(event: EVENT) {
        _event.sendEvent(event)
    }

    protected fun postEvent(event: EVENT) {
        _event.postEvent(event)
    }

}