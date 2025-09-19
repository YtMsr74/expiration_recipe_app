package com.example.tfs2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfs2.R
import com.example.tfs2.model.listener.RecipeClickListener
import com.example.tfs2.model.recipe.Recipe
import com.squareup.picasso.Picasso

class RandomRecipeAdapter(var context: Context, var list: List<Recipe>, var listener: RecipeClickListener) :
    RecyclerView.Adapter<RandomRecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomRecipeViewHolder {
        return RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RandomRecipeViewHolder, position: Int) {
        holder.text_title.text = list[position].title
        holder.text_title.isSelected = true
        holder.text_likes.text = "${list[position].aggregateLikes} Понравилось"
        holder.text_servings.text = "${list[position].servings} Порций"
        holder.text_time.text = "${list[position].readyInMinutes} Мин"
        Picasso.get().load(list[position].image).into(holder.image_dish)

        holder.random_list_container.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                listener.onRecipeClicked(list[holder.adapterPosition].id.toString())
            }
        }
        )
    }

    fun updateData(newList: List<Recipe>) {
        list = newList
        notifyDataSetChanged()
    }
}

class RandomRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val random_list_container: CardView = itemView.findViewById(R.id.random_list_container)
    val text_title: TextView = itemView.findViewById(R.id.text_title)
    val text_servings: TextView = itemView.findViewById(R.id.text_servings)
    val text_likes: TextView = itemView.findViewById(R.id.text_likes)
    val text_time: TextView = itemView.findViewById(R.id.text_time)
    val image_dish: ImageView = itemView.findViewById(R.id.image_dish)
}