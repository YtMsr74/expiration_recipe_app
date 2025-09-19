package com.example.tfs2.model.listener

import com.example.tfs2.model.recipe.InstructionResponse

interface InstructionListener {
    fun didFetch(response: List<InstructionResponse>, message: String)
    fun didError(message: String)
}