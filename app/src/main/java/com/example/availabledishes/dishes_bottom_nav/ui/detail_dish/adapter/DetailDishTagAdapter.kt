package com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.databinding.TagItemBinding
import com.example.availabledishes.dishes_bottom_nav.domain.model.DishTag

class DetailDishTagAdapter(private val clickListener: DetailDishTagListener) :
    RecyclerView.Adapter<DetailDishTagViewHolder>() {

    private var tagsList = listOf<DishTag>()

    fun setTagsList(newList: List<DishTag>) {
        tagsList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailDishTagViewHolder {
        val binding = TagItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailDishTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailDishTagViewHolder, position: Int) {
        holder.bind(tagsList[position])
        holder.itemView.setOnClickListener { clickListener.onTagClick(tagsList[position]) }
    }

    override fun getItemCount(): Int = tagsList.size

    interface DetailDishTagListener {
        fun onTagClick(tag: DishTag)
    }
}

class DetailDishTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: DishTag) {
        with(binding) {
            tagName.text = tag.name
            tag.color?.let {
                tagBg.setBackgroundColor(Color.parseColor(it))
            }
        }
    }
}