package com.example.tfs2.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: Flow<List<Item>> = itemDao.getAll()

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