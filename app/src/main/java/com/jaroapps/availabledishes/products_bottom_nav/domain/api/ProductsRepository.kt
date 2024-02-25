package com.jaroapps.availabledishes.products_bottom_nav.domain.api

import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun createNewProduct(product: Product)
    suspend fun changeProduct(product: Product, newProduct: Product)
    suspend fun toggleFavorite(product: Product)
    suspend fun toggleBuy(product: Product)
    suspend fun toggleDishFavorite(dish: Dish)
    suspend fun getAllProductsList(): List<Product>
    suspend fun deleteProduct(product: Product)
    suspend fun getMyProductsList(): List<Product>
    suspend fun getBuyProductsList(): List<Product>
    suspend fun getProductByName(productName: String): Product
    fun getProductTagList(tags: List<String>): Flow<List<Tag>>
    suspend fun getAllProductTags(): List<Tag>
    suspend fun getAllDishesWithThisProduct(product: Product): List<Dish>
    suspend fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean
}