package com.teampatch.core.domain.fake

abstract class FakeModel<T> {

    protected abstract fun build(): T
    fun get() = build()
}