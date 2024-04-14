package com.jaroapps.availabledishes.dishes_bottom_nav.domain.api

import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface DishesRepository {
    suspend fun createNewDish(dish: Dish)
    suspend fun changeDish(dishForChange: Dish, newDish: Dish)
    suspend fun toggleFavorite(dish: Dish)
    suspend fun toggleFavoriteProduct(product: Product)
    suspend fun toggleBuyProduct(product: Product)
    suspend fun deleteDish(dish: Dish)
    suspend fun getAllDishes(): List<Dish>
    suspend fun getDishByName(dishName: String): Dish
    suspend fun getDishTagList(tags: List<String>): Flow<List<Tag>>
    suspend fun getDishIngredients(products: List<String>): List<Product>
    suspend fun getAllDishesTags(): List<Tag>
    suspend fun getAvailableDishes(): List<Dish>
    suspend fun getQueryProducts(query: String): List<Product>
    suspend fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean
}