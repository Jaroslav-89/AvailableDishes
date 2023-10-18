package com.example.availabledishes.products_bottom_nav.data.storage

import com.example.availabledishes.products_bottom_nav.domain.model.Product

interface LocalStorageProducts {

    fun getAllProductsList(): List<Product>
    fun getMyProductsList(): List<Product>
    fun getBuyProductsList(): List<Product>
    fun getProductByName(productName: String): Product
    fun deleteProduct(product: Product)
    fun createNewProduct(product: Product)
    fun changeProduct(productForChange: Product, newProduct: Product)
    fun toggleFavorite(product: Product)
    fun toggleBuy(product: Product)
    fun checkingNameNewProductForMatches(newNameForChack: String): Boolean
}