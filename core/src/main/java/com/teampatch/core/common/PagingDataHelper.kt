package com.teampatch.core.common

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class PagingDataHelper<T : Any>(originPagingDataFlow: Flow<PagingData<T>>) {

    private val itemInsertFlow = MutableStateFlow<List<T>>(emptyList())
    private val itemEditFlow = MutableStateFlow<Set<ItemDiffer<T>>>(emptySet())
    private val itemDeleteFlow = MutableStateFlow<Set<T>>(emptySet())

    val pagingDataInsertedItems: Flow<List<T>> = itemInsertFlow
        .combine(itemEditFlow) { pagingData, itemEdit ->
            if (itemEdit.isEmpty()) return@combine pagingData

            pagingData.map { value ->
                itemEdit.find { it.oldItem == value }?.newItem ?: value
            }
        }
        .combine(itemDeleteFlow) { pagingData, itemDelete ->
            if (itemDelete.isEmpty()) return@combine pagingData

            pagingData.filter {
                !itemDelete.contains(it)
            }
        }

    val pagingDataFlow: Flow<PagingData<T>> = originPagingDataFlow
        .combine(itemEditFlow) { pagingData, itemEdit ->
            if (itemEdit.isEmpty()) return@combine pagingData

            pagingData.map { value ->
                itemEdit.find { it.oldItem == value }?.newItem ?: value
            }
        }
        .combine(itemDeleteFlow) { pagingData, itemDelete ->
            if (itemDelete.isEmpty()) return@combine pagingData

            pagingData.filter {
                !itemDelete.contains(it)
            }
        }

    fun addItem(newItem: T, reversed: Boolean = false) = itemInsertFlow.update {
        it.toMutableList().apply {
            if (reversed) add(0, newItem) else add(newItem)
        }
    }

    fun editItem(oldItem: T, newItem: T) = itemEditFlow.update {
        it.toMutableSet().apply {
            add(ItemDiffer(oldItem, newItem))
        }
    }

    fun deleteItem(item: T) = itemDeleteFlow.update {
        it.toMutableSet().apply {
            add(item)
        }
    }

    companion object {
        private data class ItemDiffer<T>(
            val oldItem: T,
            val newItem: T,
        )
    }
}