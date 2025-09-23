package com.example.tfs2.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.view.listener.ItemClickListener
import com.example.tfs2.databinding.ItemCellBinding
import com.example.tfs2.model.Item
import java.time.format.DateTimeFormatter

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

class ItemViewHolder(
    private val binding: ItemCellBinding,
    private val clickListener: ItemClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bindItem(item: Item) {
        binding.name.text = item.name
        binding.date.text = item.expiryDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        binding.buttonDelete.setOnClickListener {
            clickListener.deleteItem(item)
        }
        binding.itemCell.setOnClickListener {
            clickListener.editItem(item)
        }
    }
}