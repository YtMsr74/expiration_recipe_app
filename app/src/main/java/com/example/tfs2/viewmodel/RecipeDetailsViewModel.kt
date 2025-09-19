package com.example.tfs2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.tfs2.RequestManager
import com.example.tfs2.model.listener.InstructionListener
import com.example.tfs2.model.listener.RecipeDetailsListener
import com.example.tfs2.model.recipe.InstructionResponse
import com.example.tfs2.model.recipe.RecipeDetailsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(private val requestManager: RequestManager) : ViewModel() {
    val recipeDetails = MutableStateFlow<RecipeDetailsResponse?>(null)
    val instructions = MutableStateFlow<List<InstructionResponse>>(emptyList())
    val isLoading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    fun loadRecipeDetails(id: Int) {
        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            requestManager.getRecipeDetails(object : RecipeDetailsListener {
                override fun didFetch(response: RecipeDetailsResponse, message: String) {
                    recipeDetails.value = response
                    isLoading.value = false
                }

                override fun didError(message: String) {
                    error.value = message
                    isLoading.value = false
                }
            }, id)
        }
    }

    fun loadInstructions(id: Int) {
        viewModelScope.launch {
            requestManager.getInstructions(object : InstructionListener {
                override fun didFetch(response: List<InstructionResponse>, message: String) {
                    instructions.value = response
                }

                override fun didError(message: String) {
                    error.value = "Ошибка: $message"
                }
            }, id)
        }
    }
}

class RecipeDetailsViewModelFactory(private val requestManager: RequestManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailsViewModel(requestManager) as T
        }
        throw IllegalArgumentException("Unknown Class for ViewModel")
    }
}