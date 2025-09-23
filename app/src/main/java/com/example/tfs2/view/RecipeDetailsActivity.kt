package com.example.tfs2.view

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfs2.model.RecipeRequestManager
import com.example.tfs2.view.adapter.IngredientAdapter
import com.example.tfs2.view.adapter.InstructionAdapter
import com.example.tfs2.databinding.ActivityRecipeDetailsBinding
import com.example.tfs2.model.recipe.RecipeDetailsResponse
import com.example.tfs2.viewmodel.RecipeDetailsViewModel
import com.example.tfs2.viewmodel.RecipeDetailsViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class RecipeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailsBinding
    private val viewModel: RecipeDetailsViewModel by viewModels {
        RecipeDetailsViewModelFactory(RecipeRequestManager(this))
    }
    private val ingredientAdapter: IngredientAdapter = IngredientAdapter(this, emptyList())
    private val instructionAdapter: InstructionAdapter = InstructionAdapter(this, emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupObservers()

        val id = intent.getStringExtra("id")?.toIntOrNull() ?: 0
        if (id != 0) {
            viewModel.loadRecipeDetails(id)
            viewModel.loadInstructions(id)
        }
        else {
            Toast.makeText(this, "Неверный ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerDishIngredients.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RecipeDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ingredientAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launch {
            viewModel.recipeDetails.collect { recipeDetails ->
                recipeDetails?.let { updateRecipeDetails(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.instructions.collect { instructions ->
                instructionAdapter.updateData(instructions)
            }
        }
        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(this@RecipeDetailsActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateRecipeDetails(recipeDetails: RecipeDetailsResponse) {
        binding.textViewDishName.text = recipeDetails.title
        binding.textViewDishSource.text = recipeDetails.sourceName
        binding.textViewDishSummary.text = Html.fromHtml(recipeDetails.summary, Html.FROM_HTML_MODE_COMPACT)
        Picasso.get().load(recipeDetails.image).into(binding.imageViewDish)

        ingredientAdapter.updateData(recipeDetails.extendedIngredients)
    }
}