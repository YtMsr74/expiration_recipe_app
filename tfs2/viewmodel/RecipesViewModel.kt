package com.example.tfs2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tfs2.RequestManager
import com.example.tfs2.model.listener.RandomRecipeResponseListener
import com.example.tfs2.model.recipe.RandomRecipeApiResponse
import kotlinx.coroutines.flow.MutableStateFlow

class RecipesViewModel(private val requestManager: RequestManager) : ViewModel() {
    val recipes = MutableStateFlow<RandomRecipeApiResponse?>(null)
    val isLoading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    fun loadRecipes(tags: List<String> = emptyList()) {
        isLoading.value = true
        error.value = null

        requestManager.getRandomRecipes(object : RandomRecipeResponseListener {
            override fun didFetch(response: RandomRecipeApiResponse?, message: String) {
                recipes.value = response
                isLoading.value = false
            }

            override fun didError(message: String) {
                error.value = message
                isLoading.value = false
            }
        }, tags)
    }
}