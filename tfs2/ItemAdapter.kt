package com.example.tfs2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.databinding.ItemCellBinding
import com.example.tfs2.model.Item

class ItemAdapter(
    private val items: List<Item>,
    private val clickListener: ItemClickListener
): RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemCellBinding.inflate(from, parent, false)
        return ItemViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size
}