package com.example.tfs2.view.listener

import com.example.tfs2.model.recipe.RecipeDetailsResponse

interface RecipeDetailsListener {
    fun didFetch(response: RecipeDetailsResponse, message: String)
    fun didError(message: String)
}