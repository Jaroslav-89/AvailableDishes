package com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.databinding.RefrigeratorItemBinding
import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Refrigerator
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product

class MyRefrigeratorsAdapter(private val clickListener: MyRefrigeratorsClickListener) :
    RecyclerView.Adapter<MyRefrigeratorsViewHolder>() {

    private var myRefrigeratorsList = emptyList<Refrigerator>()

    fun setRefrigeratorsList(
        newList: List<Refrigerator>
    ) {
        val diffResult =
            DiffUtil.calculateDiff(RefrigeratorsDiffCallback(myRefrigeratorsList, newList))
        myRefrigeratorsList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRefrigeratorsViewHolder {
        val binding = RefrigeratorItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyRefrigeratorsViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MyRefrigeratorsViewHolder, position: Int) {
        holder.bind(myRefrigeratorsList[position])
    }

    override fun getItemCount(): Int = myRefrigeratorsList.size

    interface MyRefrigeratorsClickListener {
        fun onRefrigeratorClick(refrigerator: Refrigerator)
    }
}

class MyRefrigeratorsViewHolder(
    private val binding: RefrigeratorItemBinding,
    private val clickListener: MyRefrigeratorsAdapter.MyRefrigeratorsClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(refrigerator: Refrigerator) {
        with(binding) {
            nameTv.text = refrigerator.name
            noteValueTv.text = refrigerator.numberOfNotes.toString()
            Glide.with(itemView)
                .load(refrigerator.imgUrl)
                .placeholder(R.drawable.refrigerator_image_placeholder)
                .into(refrigeratorImg)
        }
    }
}