package com.example.tfs2.model.listener

import com.example.tfs2.model.recipe.RandomRecipeApiResponse

interface RandomRecipeResponseListener {
    fun didFetch(response: RandomRecipeApiResponse?, message: String)
    fun didError(message: String)
}