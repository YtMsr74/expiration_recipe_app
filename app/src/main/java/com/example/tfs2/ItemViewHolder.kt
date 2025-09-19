package com.example.tfs2

import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.databinding.ItemCellBinding
import com.example.tfs2.model.Item
import java.time.format.DateTimeFormatter

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