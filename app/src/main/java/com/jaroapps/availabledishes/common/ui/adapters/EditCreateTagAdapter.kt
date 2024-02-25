package com.jaroapps.availabledishes.common.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.databinding.TagItemBinding

class CreateEditTagAdapter(private val clickListener: DeleteTagListener) :
    RecyclerView.Adapter<CreateEditTagViewHolder>() {

    private var tagsList = emptyList<Tag>()

    fun setTagsList(newList: List<Tag>) {
        tagsList = newList
        notifyDataSetChanged()
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
        fun onTagClick(tag: Tag)
    }
}

class CreateEditTagViewHolder(
    private val binding: TagItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tag: Tag) {
        with(binding) {
            tagName.text = tag.name
            tagBg.setBackgroundColor(Color.parseColor(tag.color))
            deleteTag.visibility = View.VISIBLE
        }
    }
}