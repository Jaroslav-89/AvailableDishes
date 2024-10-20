package com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Refrigerator
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product

class RefrigeratorsDiffCallback(
    private val oldList: List<Refrigerator>,
    private val newList: List<Refrigerator>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
