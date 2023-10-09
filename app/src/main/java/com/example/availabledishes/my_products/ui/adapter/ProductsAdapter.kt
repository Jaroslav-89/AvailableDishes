package com.example.availabledishes.my_products.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.availabledishes.R
import com.example.availabledishes.databinding.ProductItemBinding
import com.example.availabledishes.my_products.domain.model.Product

class ProductsAdapter(private val clickListener: ProductClickListener) :
    RecyclerView.Adapter<ProductViewHolder>() {

    private var productsList = ArrayList<Product>()

    fun setProductsList(newList: ArrayList<Product>) {
        productsList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size

    interface ProductClickListener {
        //fun onProductClick(product: Product)
        fun onFavoriteToggleClick(product: Product)
    }
}

class ProductViewHolder(
    private val binding: ProductItemBinding,
    private val clickListener: ProductsAdapter.ProductClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        with(binding) {
            productName.text = product.name
            favorite.setImageDrawable(getFavoriteToggleDrawable(product.inFavorite))
            favorite.setOnClickListener { clickListener.onFavoriteToggleClick(product) }
        }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? {
        return itemView.context.getDrawable(
            if (inFavorite) R.drawable.active_favorite else R.drawable.inactive_favorite
        )
    }
}