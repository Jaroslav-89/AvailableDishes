package com.example.availabledishes.products_bottom_nav.data

import com.example.availabledishes.products_bottom_nav.data.storage.LocalStorageProducts
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class ProductsRepositoryImpl(private val localStorage: LocalStorageProducts) : ProductsRepository {
    override fun getAllProductsList(): List<Product> {
        return localStorage.getAllProductsList()
    }

    override fun getMyProductsList(): List<Product> {
        return localStorage.getMyProductsList()
    }

    override fun getBuyProductsList(): List<Product> {
        return localStorage.getBuyProductsList()
    }

    override fun getProductByName(productName: String): Product {
        return localStorage.getProductByName(productName)
    }

    override fun deleteProduct(product: Product) {
        localStorage.deleteProduct(product)
    }

    override fun createNewProduct(product: Product) {
        localStorage.createNewProduct(product)
    }

    override fun changeProduct(product: Product, newProduct: Product) {
        localStorage.changeProduct(product, newProduct)
    }

    override fun toggleFavorite(product: Product) {
        localStorage.toggleFavorite(product)
    }

    override fun toggleBuy(product: Product) {
        localStorage.toggleBuy(product)
    }

    override fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean {
        return localStorage.checkingNameNewProductForMatches(newNameForCheck)
    }
}