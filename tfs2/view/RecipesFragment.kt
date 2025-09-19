package com.example.tfs2.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.R
import com.example.tfs2.RequestManager
import com.example.tfs2.adapter.RandomRecipeAdapter
import com.example.tfs2.databinding.FragmentRecipesBinding
import com.example.tfs2.model.ItemDao
import com.example.tfs2.model.ItemDatabase
import com.example.tfs2.model.listener.RandomRecipeResponseListener
import com.example.tfs2.model.listener.RecipeClickListener
import com.example.tfs2.model.recipe.RandomRecipeApiResponse
import com.example.tfs2.viewmodel.RecipesViewModel
import com.example.tfs2.viewmodel.ScanViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecipesFragment: Fragment(R.layout.fragment_recipes) {
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var randomRecipeAdapter: RandomRecipeAdapter
    private lateinit var viewModel: RecipesViewModel
    private lateinit var itemDao: ItemDao

    private val selectedProducts = mutableListOf<String>()
    private val tags = mutableListOf<String>()

    private val recipeClickListener = object : RecipeClickListener {
        override fun onRecipeClicked(id: String) {
            startActivity(Intent(requireContext(), RecipeDetailsActivity::class.java)
                .putExtra("id", id))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipesBinding.bind(view)

        val db = ItemDatabase.getDatabase(requireContext())
        itemDao = db.itemDao()

        val manager = RequestManager(requireContext())
        viewModel = RecipesViewModel(manager)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.loadRecipes(emptyList())
    }

    private fun setupRecyclerView() {
        randomRecipeAdapter = RandomRecipeAdapter(requireContext(), emptyList(), recipeClickListener)
        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = randomRecipeAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.recipes.collect { response ->
                response?.let { updateRecipes(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSelectProducts.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val products = itemDao.getAll().first()
                    val names = products.map { it.name }.toTypedArray()
                    val checked = BooleanArray(products.size) { index ->
                        selectedProducts.contains(products[index].name)
                    }
                    AlertDialog.Builder(requireContext())
                        .setTitle("Ингредиенты")
                        .setMultiChoiceItems(names, checked) { _, i, isChecked ->
                            val name = products[i].name
                            if (isChecked) {
                                selectedProducts.add(name)
                            } else {
                                selectedProducts.remove(name)
                            }
                        }
                        .setPositiveButton("Применить") { dialog, _ ->
                            updateSelectedProductsChips()
                            performSearchWithProducts()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Отмена", null)
                        .show()
                } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Ошибка загрузки продуктов", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.searchRecipe.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean = true
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    tags.clear()
                    tags.add(it)
                    viewModel.loadRecipes(tags)
                }
                return true
            }
        })
    }

    private fun updateRecipes(response: RandomRecipeApiResponse) {
        randomRecipeAdapter.updateData(response.recipes ?: emptyList())
    }

    private fun updateSelectedProductsChips() {
        binding.chipGroupProducts.removeAllViews()

        selectedProducts.forEach { name ->
            val chip = Chip(requireContext()).apply {
                text = name
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    selectedProducts.remove(name)
                    updateSelectedProductsChips()
                    performSearchWithProducts()
                }
            }
            binding.chipGroupProducts.addView(chip)
        }
    }

    private fun performSearchWithProducts() {
        tags.clear()
        tags.addAll(selectedProducts)
        viewModel.loadRecipes(tags)
    }
}