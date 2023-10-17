package com.example.availabledishes.products_bottom_nav.data

import android.content.Intent
import android.provider.MediaStore
import com.example.availabledishes.products_bottom_nav.data.storage.LocalStorage
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.fragment.EditCreateProductFragment

class ProductsRepositoryImpl(private val localStorage: LocalStorage) : ProductsRepository {
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

    override fun checkingNameNewProductForMatches(newNameForChack: String): Boolean {
        return localStorage.checkingNameNewProductForMatches(newNameForChack)
    }
}