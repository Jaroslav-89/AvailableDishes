package com.jaroapps.availabledishes.products_bottom_nav.domain.api

import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun createNewProduct(product: Product)
    suspend fun changeProduct(product: Product, newProduct: Product)
    suspend fun toggleFavorite(product: Product)
    suspend fun toggleBuy(product: Product)
    suspend fun toggleDishFavorite(dish: Dish)
    suspend fun getAllProducts(): List<Product>
    fun getAllProductsList(): Flow<List<ProductList>>
    suspend fun deleteProduct(product: Product)
    fun getProductsInList(listId: String): Flow<List<Product>>
    suspend fun getBuyProductsList(): List<Product>
    suspend fun getProductByName(productName: String): Product
    fun getProductTagList(tags: List<String>): Flow<List<Tag>>
    fun getAllProductTags(): Flow<List<Tag>>
    suspend fun getAllDishesWithThisProduct(product: Product): List<Dish>
    suspend fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean
}