package com.example.tfs2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.R
import com.example.tfs2.model.recipe.ExtendedIngredient
import com.example.tfs2.model.recipe.Recipe
import com.squareup.picasso.Picasso

class IngredientAdapter(var context: Context, var list: List<ExtendedIngredient>): RecyclerView.Adapter<IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.list_dish_ingredients, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.textView_ingredient_name.text = list[position].name
        holder.textView_ingredient_name.isSelected = true
        holder.textView_ingredient_quantity.text = list[position].original
        holder.textView_ingredient_quantity.isSelected = true
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/" + list[position].image).into(holder.imageView_ingredient)
    }

    fun updateData(newList: List<ExtendedIngredient>) {
        list = newList
        notifyDataSetChanged()
    }
}

class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView_ingredient_quantity: TextView = itemView.findViewById(R.id.textView_ingredient_quantity)
    val imageView_ingredient: ImageView = itemView.findViewById(R.id.imageView_ingredient)
    val textView_ingredient_name: TextView = itemView.findViewById(R.id.textView_ingredient_name)
}