package com.jaroapps.availabledishes.products_bottom_nav.domain.api

import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun editCreateProductList(productList: ProductList)
    suspend fun getProductListById(productListId: String): ProductList
    fun getAllProductLists(): Flow<List<ProductList>>
    suspend fun deleteProductList(productListId: String)

    suspend fun createNewProduct(product: Product)
    suspend fun changeProduct(product: Product, newProduct: Product)
    suspend fun toggleFavorite(product: Product)
    suspend fun toggleBuy(product: Product)
    suspend fun toggleDishFavorite(dish: Dish)
    suspend fun getAllProducts(): List<Product>
    suspend fun toggleAddProductToList(productName: String, productListId: String)

    suspend fun deleteProduct(product: Product)
    suspend fun getProductsInList(listId: String): List<Product>
    suspend fun getBuyProductsList(): List<Product>
    suspend fun getProductByName(productName: String): Product
    fun getProductTagList(tags: List<String>): Flow<List<Tag>>
    fun getAllProductTags(): Flow<List<Tag>>
    suspend fun getAllDishesWithThisProduct(product: Product): List<Dish>
    suspend fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean
}