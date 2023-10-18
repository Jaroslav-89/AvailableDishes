package com.example.availabledishes.products_bottom_nav.domain.impl

import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class ProductsInteractorImpl(private val repository: ProductsRepository) : ProductsInteractor {

    override fun getAllProductsList(): List<Product> {
        return repository.getAllProductsList()
    }

    override fun getMyProductsList(): List<Product> {
        return repository.getMyProductsList()
    }

    override fun getBuyProductsList(): List<Product> {
        return repository.getBuyProductsList()
    }

    override fun getProductByName(productName: String): Product {
        return repository.getProductByName(productName)
    }

    override fun deleteProduct(product: Product) {
        repository.deleteProduct(product)
    }

    override fun createNewProduct(product: Product) {
        repository.createNewProduct(product)
    }

    override fun changeProduct(product: Product, newProduct: Product) {
        repository.changeProduct(product, newProduct)
    }

    override fun toggleFavorite(product: Product) {
        repository.toggleFavorite(product)
    }

    override fun toggleBuy(product: Product) {
        repository.toggleBuy(product)
    }

    override fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean {
        return repository.checkingNameNewProductForMatches(newNameForCheck)
    }
}