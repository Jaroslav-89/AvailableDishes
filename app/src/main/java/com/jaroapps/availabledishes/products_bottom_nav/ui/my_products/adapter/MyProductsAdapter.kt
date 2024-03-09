package com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.ui.adapters.ProductsDiffCallback
import com.jaroapps.availabledishes.databinding.MyProductItemBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product

class MyProductsAdapter(private val clickListener: MyProductClickListener) :
    RecyclerView.Adapter<MyProductViewHolder>() {

    private var myProductsList = emptyList<Product>()

    fun setProductsList(
        newList: List<Product>
    ) {
        val diffResult = DiffUtil.calculateDiff(ProductsDiffCallback(myProductsList, newList))
        myProductsList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductViewHolder {
        val binding = MyProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyProductViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MyProductViewHolder, position: Int) {
        holder.bind(myProductsList[position])
        holder.itemView.setOnClickListener { clickListener.onProductClick(myProductsList[position]) }
    }

    override fun getItemCount(): Int = myProductsList.size

    interface MyProductClickListener {
        fun onProductClick(product: Product)
        fun onFavoriteToggleClick(product: Product)
        fun onBuyToggleClick(product: Product)
    }
}

class MyProductViewHolder(
    private val binding: MyProductItemBinding,
    private val clickListener: MyProductsAdapter.MyProductClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        with(binding) {
            productName.text = product.name
            needToBuy.setImageDrawable(getBuyToggleDrawable(product.needToBuy))
            needToBuy.setOnClickListener { clickListener.onBuyToggleClick(product) }
        }
    }

    private fun getBuyToggleDrawable(needToBuy: Boolean?): Drawable? {
        return if (needToBuy == null || !needToBuy)
            getDrawable(itemView.context, R.drawable.ic_product_available)
        else
            getDrawable(itemView.context, R.drawable.ic_need_to_buy)
    }
}