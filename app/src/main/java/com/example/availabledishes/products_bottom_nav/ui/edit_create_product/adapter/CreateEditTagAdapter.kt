package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.databinding.TagItemBinding
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

class CreateEditTagAdapter(private val clickListener: DeleteTagListener) :
    RecyclerView.Adapter<CreateEditTagViewHolder>() {

    private var tagsList = mutableListOf<ProductTag>()

    fun setTagsList(newList: List<ProductTag>) {
        tagsList = newList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateEditTagViewHolder {
        val binding = TagItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CreateEditTagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreateEditTagViewHolder, position: Int) {
        holder.bind(tagsList[position])
        holder.itemView.setOnClickListener { clickListener.onTagClick(tagsList[position]) }
    }

    override fun getItemCount(): Int = tagsList.size

    interface DeleteTagListener {
        fun onTagClick(tag: ProductTag)
    }
}

class CreateEditTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: ProductTag) {
        with(binding) {
            tagName.text = tag.name
            tag.color?.let {
                tagBg.setBackgroundColor(Color.parseColor(tag.color))
                deleteTag.visibility = View.VISIBLE
            }
        }
    }
}