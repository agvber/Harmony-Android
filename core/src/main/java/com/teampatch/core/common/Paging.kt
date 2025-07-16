package com.teampatch.core.common

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T : Any> Throwable.toPagingDataFlow(): Flow<PagingData<T>> = flowOf(toPagingData())

fun <T : Any> Throwable.toPagingData(): PagingData<T> {
    val errorLoadStates = LoadStates(
        refresh = LoadState.Error(this),
        prepend = LoadState.Error(this),
        append = LoadState.Error(this)
    )
    return PagingData.empty(errorLoadStates)
}

fun <T : Any> LazyPagingItems<T>.getOrNull(index: Int): T? = kotlin.runCatching { get(index) }.getOrNull()