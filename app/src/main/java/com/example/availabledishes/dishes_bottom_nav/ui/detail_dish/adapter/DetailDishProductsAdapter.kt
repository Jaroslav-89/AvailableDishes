package com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.R
import com.example.availabledishes.databinding.DishDetailProductItemBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class DetailDishProductsAdapter(private val clickListener: ProductClickListener) :
    RecyclerView.Adapter<DetailDishProductViewHolder>() {

    private var productsList = listOf<Product>()

    fun setProductsList(newList: List<Product>) {
        productsList =
            newList.sortedBy { it.name.lowercase() }
                .sortedByDescending { it.needToBuy == false || it.needToBuy == null }
                .sortedByDescending { it.inFavorite == true }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailDishProductViewHolder {
        val binding = DishDetailProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailDishProductViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: DetailDishProductViewHolder, position: Int) {
        holder.bind(productsList[position])
        holder.itemView.setOnClickListener { clickListener.onProductClick(productsList[position]) }
    }

    override fun getItemCount(): Int = productsList.size

    interface ProductClickListener {
        fun onProductClick(product: Product)
        fun onFavoriteToggleClick(product: Product)
        fun onBuyToggleClick(product: Product)
    }
}

class DetailDishProductViewHolder(
    private val binding: DishDetailProductItemBinding,
    private val clickListener: DetailDishProductsAdapter.ProductClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        with(binding) {
            productName.text = product.name
            favorite.setImageDrawable(getFavoriteToggleDrawable(product.inFavorite))
            favorite.setOnClickListener { clickListener.onFavoriteToggleClick(product) }
            if (product.inFavorite == true) {
                needToBuy.visibility = View.VISIBLE
                needToBuy.setImageDrawable(getBuyToggleDrawable(product.needToBuy))
                needToBuy.setOnClickListener { clickListener.onBuyToggleClick(product) }
            } else {
                needToBuy.visibility = View.GONE
            }

        }

    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return itemView.context.getDrawable(
            if (inFavorite == null || !inFavorite) R.drawable.ic_inactive_favorite else R.drawable.ic_active_favorite
        )
    }

    private fun getBuyToggleDrawable(needToBuy: Boolean?): Drawable? {
        return itemView.context.getDrawable(
            if (needToBuy == null || !needToBuy) R.drawable.ic_product_available else R.drawable.ic_need_to_buy
        )
    }
}