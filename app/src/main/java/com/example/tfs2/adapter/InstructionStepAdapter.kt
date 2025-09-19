package com.example.tfs2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.R
import com.example.tfs2.model.recipe.Step

class InstructionStepAdapter(var context: Context, var list: List<Step>) : RecyclerView.Adapter<InstructionStepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionStepViewHolder {
        return InstructionStepViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instruction_steps, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: InstructionStepViewHolder, position: Int) {
        holder.textView_step_number.text = list[position].number.toString()
        holder.textView_step_title.text = list[position].step
    }

}

class InstructionStepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView_step_number: TextView = itemView.findViewById(R.id.textView_step_number)
    var textView_step_title: TextView = itemView.findViewById(R.id.textView_step_title)
}