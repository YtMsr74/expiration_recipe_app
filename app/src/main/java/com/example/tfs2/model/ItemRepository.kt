package com.example.tfs2.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: StateFlow<List<Item>> = itemDao.getAll()
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @WorkerThread
    suspend fun insertItem(item: Item){
        itemDao.insert(item)
    }

    @WorkerThread
    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }

    @WorkerThread
    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }
}