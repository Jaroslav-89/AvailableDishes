package com.example.availabledishes.products_bottom_nav.ui.detail_product.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.databinding.TagItemBinding
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

class DetailProductTagAdapter(private val clickListener: DetailProductTagListener) :
    RecyclerView.Adapter<DetailProductTagViewHolder>() {

    private var tagsList = listOf<ProductTag>()

    fun setTagsList(newList: List<ProductTag>) {
        tagsList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailProductTagViewHolder {
        val binding = TagItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailProductTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailProductTagViewHolder, position: Int) {
        holder.bind(tagsList[position])
        holder.itemView.setOnClickListener { clickListener.onTagClick(tagsList[position]) }
    }

    override fun getItemCount(): Int = tagsList.size

    interface DetailProductTagListener {
        fun onTagClick(tag: ProductTag)
    }
}

class DetailProductTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: ProductTag) {
        with(binding) {
            tagName.text = tag.name
            tag.color?.let {
                tagBg.setBackgroundColor(Color.parseColor(it))
            }
        }
    }
}