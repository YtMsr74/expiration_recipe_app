package com.example.tfs2.view.listener

import com.example.tfs2.model.ItemCodeResponse

interface ItemInfoListener {
    fun didFetch(response: ItemCodeResponse?, message: String)
    fun didError(message: String)
}