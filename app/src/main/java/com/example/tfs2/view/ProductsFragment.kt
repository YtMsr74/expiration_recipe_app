package com.example.tfs2.view

import android.os.Bundle
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.fragment.app.Fragment
import com.example.tfs2.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfs2.view.adapter.ItemAdapter
import com.example.tfs2.view.listener.ItemClickListener
import com.example.tfs2.databinding.FragmentProductsBinding
import com.example.tfs2.util.ProductApplication
import com.example.tfs2.model.Item
import com.example.tfs2.viewmodel.ItemModelFactory
import com.example.tfs2.viewmodel.ItemViewModel
import kotlinx.coroutines.launch

@ExperimentalGetImage
class ProductsFragment: Fragment(R.layout.fragment_products), ItemClickListener {

    private lateinit var binding: FragmentProductsBinding
    private val itemViewModel: ItemViewModel by viewModels {
        ItemModelFactory((requireActivity().application as ProductApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductsBinding.bind(view)
        binding.buttonAdd.setOnClickListener {
            NewProductSheet(null).show(childFragmentManager, "newItemTag")
        }
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val fragment = this
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                itemViewModel.items.collect { items ->
                    binding.productList.apply {
                        layoutManager = LinearLayoutManager(requireContext().applicationContext)
                        adapter = ItemAdapter(items, fragment)
                    }
                }
            }
        }
    }

    override fun editItem(item: Item) {
        NewProductSheet(item).show(childFragmentManager, "newItemTag")
    }

    override fun deleteItem(item: Item) {
        itemViewModel.deleteItem(item)
    }
}