package com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList

class AllProductListsDiffCallback(
    private val oldList: List<ProductList>,
    private val newList: List<ProductList>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
