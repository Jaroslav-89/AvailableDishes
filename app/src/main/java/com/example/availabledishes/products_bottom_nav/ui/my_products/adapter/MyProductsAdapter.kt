package com.example.availabledishes.products_bottom_nav.ui.my_products.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.R
import com.example.availabledishes.databinding.MyProductItemBinding
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class MyProductsAdapter(private val clickListener: MyProductClickListener) :
    RecyclerView.Adapter<MyProductViewHolder>() {

    private var myProductsList = listOf<Product>()

    fun setProductsList(newList: List<Product>) {
        myProductsList = newList
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
            //favorite.setImageDrawable(getFavoriteToggleDrawable(product.inFavorite))
            // favorite.setOnClickListener { clickListener.onFavoriteToggleClick(product) }
            needToBuy.setImageDrawable(getBuyToggleDrawable(product.needToBuy))
            needToBuy.setOnClickListener { clickListener.onBuyToggleClick(product) }
            //itemView.setOnClickListener { clickListener.onProductClick(product) }
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