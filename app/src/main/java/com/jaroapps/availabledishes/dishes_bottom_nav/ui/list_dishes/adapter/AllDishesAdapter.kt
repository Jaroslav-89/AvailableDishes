package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.ui.adapters.DishesDiffCallback
import com.jaroapps.availabledishes.databinding.DishItemBinding
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish

class AllDishesAdapter(private val clickListener: DishClickListener) :
    RecyclerView.Adapter<DishesViewHolder>() {

    private var dishesList = emptyList<Dish>()

    fun setDishesList(newList: List<Dish>) {
        val diffResult = DiffUtil.calculateDiff(DishesDiffCallback(dishesList, newList))
        dishesList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishesViewHolder {
        val binding = DishItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DishesViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: DishesViewHolder, position: Int) {
        holder.bind(dishesList[position])
        holder.itemView.setOnClickListener { clickListener.onDishClick(dishesList[position]) }
    }

    override fun getItemCount(): Int = dishesList.size

    interface DishClickListener {
        fun onDishClick(dish: Dish)
        fun onFavoriteToggleClick(dish: Dish)
    }
}

class DishesViewHolder(
    private val binding: DishItemBinding,
    private val clickListener: AllDishesAdapter.DishClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dish: Dish) {
        with(binding) {
            dishName.text = dish.name
            favoriteDish.setImageDrawable(getFavoriteToggleDrawable(dish.inFavorite))
            favoriteDish.setOnClickListener { clickListener.onFavoriteToggleClick(dish) }
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return itemView.context.getDrawable(
            if (inFavorite == null || !inFavorite) R.drawable.ic_inactive_favorite else R.drawable.ic_active_favorite
        )
    }
}