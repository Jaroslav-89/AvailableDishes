package com.example.availabledishes.products_bottom_nav.ui.detail_product.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.R
import com.example.availabledishes.databinding.DishItemBinding
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish

class AvailableDishesAdapter(private val clickListener: DishClickListener) :
    RecyclerView.Adapter<AvailableDishesViewHolder>() {

    private var dishesList = listOf<Dish>()

    fun setDishesList(newList: List<Dish>) {
        dishesList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableDishesViewHolder {
        val binding = DishItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AvailableDishesViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: AvailableDishesViewHolder, position: Int) {
        holder.bind(dishesList[position])
        holder.itemView.setOnClickListener { clickListener.onDishClick(dishesList[position]) }
    }

    override fun getItemCount(): Int = dishesList.size

    interface DishClickListener {
        fun onDishClick(dish: Dish)
        fun onDishFavoriteToggleClick(dish: Dish)
    }
}

class AvailableDishesViewHolder(
    private val binding: DishItemBinding,
    private val clickListener: AvailableDishesAdapter.DishClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dish: Dish) {
        with(binding) {
            dishName.text = dish.name
            favoriteDish.setImageDrawable(getFavoriteToggleDrawable(dish.inFavorite))
            favoriteDish.setOnClickListener { clickListener.onDishFavoriteToggleClick(dish) }
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return itemView.context.getDrawable(
            if (inFavorite == null || !inFavorite) R.drawable.ic_inactive_favorite else R.drawable.ic_active_favorite
        )
    }
}