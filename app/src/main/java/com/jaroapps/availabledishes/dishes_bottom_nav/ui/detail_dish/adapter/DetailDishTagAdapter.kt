package com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.databinding.TagItemBinding

class DetailDishTagAdapter(private val clickListener: DetailDishTagListener) :
    RecyclerView.Adapter<DetailDishTagViewHolder>() {

    private var tagsList = listOf<Tag>()

    fun setTagsList(newList: List<Tag>) {
        tagsList = newList
        notifyDataSetChanged()
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
        fun onTagClick(tag: Tag)
    }
}

class DetailDishTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: Tag) {
        with(binding) {
            tagName.text = tag.name
            tag.color?.let {
                tagBg.setBackgroundColor(Color.parseColor(it))
            }
        }
    }
}