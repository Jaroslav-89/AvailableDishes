package com.jaroapps.availabledishes.products_bottom_nav.domain.impl

import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.flow.Flow

class ProductsInteractorImpl(private val repository: ProductsRepository) : ProductsInteractor {

    override suspend fun getAllProductsList(): List<Product> {
        return repository.getAllProductsList()
    }

    override suspend fun getMyProductsList(): List<Product> {
        return repository.getMyProductsList()
    }

    override suspend fun getBuyProductsList(): List<Product> {
        return repository.getBuyProductsList()
    }

    override suspend fun getProductByName(productName: String): Product {
        return repository.getProductByName(productName)
    }

    override fun getProductTagList(tags: List<String>): Flow<List<Tag>> {
        return repository.getProductTagList(tags)
    }

    override suspend fun getAllProductTags(): List<Tag> {
        return repository.getAllProductTags()
    }

    override suspend fun getAllDishesWithThisProduct(product: Product): List<Dish> {
        return repository.getAllDishesWithThisProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        repository.deleteProduct(product)
    }

    override suspend fun createNewProduct(product: Product) {
        repository.createNewProduct(product)
    }

    override suspend fun changeProduct(product: Product, newProduct: Product) {
        repository.changeProduct(product, newProduct)
    }

    override suspend fun toggleFavorite(product: Product) {
        repository.toggleFavorite(product)
    }

    override suspend fun toggleBuy(product: Product) {
        repository.toggleBuy(product)
    }

    override suspend fun toggleDishFavorite(dish: Dish) {
        repository.toggleDishFavorite(dish)
    }

    override suspend fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean {
        return repository.checkingNameNewProductForMatches(newNameForCheck)
    }
}