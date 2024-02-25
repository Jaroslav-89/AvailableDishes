package com.jaroapps.availabledishes.common.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.databinding.TagItemBinding

class AddTagAdapter(private val clickListener: AddTagListener) :
    RecyclerView.Adapter<AddTagViewHolder>() {

    private var tagsList = emptyList<Tag>()

    fun setTagsList(newList: List<Tag>) {
        tagsList = newList
        notifyDataSetChanged()
    }

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
        fun onTagClick(tag: Tag)
    }
}

class AddTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: Tag) {
        with(binding) {
            tagName.text = tag.name
            tagBg.setBackgroundColor(Color.parseColor(tag.color))
            deleteTag.visibility = View.GONE

        }
    }
}