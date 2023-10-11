package com.example.availabledishes.products_bottom_nav.domain.api

import com.example.availabledishes.products_bottom_nav.domain.model.Product

interface ProductsInteractor {

    fun getAllProductsList(): List<Product>
    fun getMyProductsList(): List<Product>
    fun getBuyProductsList(): List<Product>
    fun getProductByName(productName: String): Product
    fun deleteProduct(product: Product)
    fun createNewProduct(product: Product)
    fun changeProduct(product: Product, changeProduct: Product)
    fun toggleFavorite(product: Product)
    fun toggleBuy(product: Product)
}