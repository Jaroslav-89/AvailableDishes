package com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.availabledishes.R
import com.jaroapps.availabledishes.common.ui.adapters.ProductsDiffCallback
import com.jaroapps.availabledishes.databinding.ProductItemBinding
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product

class ProductsAdapter(private val clickListener: ProductClickListener) :
    RecyclerView.Adapter<ProductViewHolder>() {

    private var productsList = emptyList<Product>()

    fun setProductsList(
        newList: List<Product>
    ) {
        val diffResult = DiffUtil.calculateDiff(ProductsDiffCallback(productsList, newList))
        productsList = newList
        diffResult.dispatchUpdatesTo(this)
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
        holder.itemView.setOnClickListener { clickListener.onProductClick(productsList[position]) }
    }

    override fun getItemCount(): Int = productsList.size

    interface ProductClickListener {
        fun onProductClick(product: Product)
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

    private fun getFavoriteToggleDrawable(inFavorite: Boolean?): Drawable? {
        return if (inFavorite == null || !inFavorite)
            getDrawable(itemView.context, R.drawable.ic_inactive_favorite)
        else
            getDrawable(itemView.context, R.drawable.ic_active_favorite)
    }
}