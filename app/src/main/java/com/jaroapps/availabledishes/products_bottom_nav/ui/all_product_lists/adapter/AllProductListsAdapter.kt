package com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.databinding.ProductListsItemBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList

class AllProductListsAdapter(private val clickListener: AllProductListsClickListener) :
    RecyclerView.Adapter<AllProductListsViewHolder>() {

    private var allProductLists = emptyList<ProductList>()

    fun setAllProductLists(newList: List<ProductList>) {
        val diffResult =
            DiffUtil.calculateDiff(AllProductListsDiffCallback(allProductLists, newList))
        allProductLists = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductListsViewHolder {
        val binding = ProductListsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllProductListsViewHolder(binding)
    }

    override fun getItemCount(): Int = allProductLists.size

    override fun onBindViewHolder(holder: AllProductListsViewHolder, position: Int) {
        holder.bind(allProductLists[position])
        holder.itemView.setOnClickListener { clickListener.onProductsListClick(allProductLists[position]) }
    }

    interface AllProductListsClickListener {
        fun onProductsListClick(productLists: ProductList)
    }
}

class AllProductListsViewHolder(
    private val binding: ProductListsItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(productLists: ProductList) {
        with(binding) {
            // productListImg
            productListNameTv.text = productLists.name
            productCountTv.text = productLists.numberOfProducts.toString()
        }
    }
}