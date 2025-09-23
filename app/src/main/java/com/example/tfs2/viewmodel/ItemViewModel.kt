package com.example.tfs2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tfs2.model.Item
import com.example.tfs2.model.ItemRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ItemViewModel(private val repository: ItemRepository): ViewModel() {

    val items: StateFlow<List<Item>> = repository.allItems

    fun addItem(newItem: Item) = viewModelScope.launch {
        repository.insertItem(newItem)
    }

    fun updateItem(item: Item) = viewModelScope.launch {
        repository.updateItem(item)
    }

    fun deleteItem(item: Item) = viewModelScope.launch {
        repository.deleteItem(item)
    }

    fun checkSoonExpiry(context: Context) {
        viewModelScope.launch {
            val currentItems = items.value ?: return@launch
            val today = LocalDate.now()
        }
    }

}

class ItemModelFactory(private val repository: ItemRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown Class for ViewModel")
    }
}