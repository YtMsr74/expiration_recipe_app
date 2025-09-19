package com.example.tfs2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.R
import com.example.tfs2.model.recipe.InstructionResponse
import com.example.tfs2.model.recipe.Recipe

class InstructionAdapter(var context: Context, var list: List<InstructionResponse>) : RecyclerView.Adapter<InstructionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        return InstructionViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.textView_instruction_name.text = (list[position].name)
        holder.recycler_instruction_steps.setHasFixedSize(true)
        holder.recycler_instruction_steps.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val stepAdapter = InstructionStepAdapter(context, list[position].steps)
        holder.recycler_instruction_steps.adapter = stepAdapter
    }

    fun updateData(newList: List<InstructionResponse>) {
        list = newList
        notifyDataSetChanged()
    }
}

class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView_instruction_name: TextView = itemView.findViewById(R.id.textView_instruction_name)
    var recycler_instruction_steps: RecyclerView = itemView.findViewById(R.id.recycler_instruction_steps)
}