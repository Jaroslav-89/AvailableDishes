package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.databinding.TagItemBinding
import com.example.availabledishes.products_bottom_nav.data.storage.AllProductsTag
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

class AddTagAdapter(private val clickListener: AddTagListener) :
    RecyclerView.Adapter<AddTagViewHolder>() {

    private var tagsList = AllProductsTag().allTags
//TODO
//    fun setTagsList(newList: List<ProductTag>) {
//        tagsList = AllProductsTag().allTags
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTagViewHolder {
        val binding = TagItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddTagViewHolder, position: Int) {
        holder.bind(tagsList[position])
        holder.itemView.setOnClickListener { clickListener.onTagClick(tagsList[position]) }
    }

    override fun getItemCount(): Int = tagsList.size

    interface AddTagListener {
        fun onTagClick(tag: ProductTag)
    }
}

class AddTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: ProductTag) {
        with(binding) {
            tagName.text = tag.name
            tag.color?.let {
                tagBg.setBackgroundColor(Color.parseColor(tag.color))
                deleteTag.visibility = View.GONE
            }
        }
    }
}