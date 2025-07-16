package com.teampatch.core.common

abstract class SingletonInstanceHelper<T> {

    protected var value: T? = null

    protected abstract fun buildInstance(): T

    fun getInstance(): T {
        if (value == null) {
            value = buildInstance()
        }
        return value!!
    }

    fun resetInstance() {
        value = null
    }
}