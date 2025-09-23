package com.example.tfs2.view.listener

import com.example.tfs2.model.Item

interface ItemClickListener {
    fun editItem(item: Item)
    fun deleteItem(item: Item)
}
