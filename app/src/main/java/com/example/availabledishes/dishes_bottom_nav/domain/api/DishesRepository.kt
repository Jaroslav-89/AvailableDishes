package com.example.availabledishes.dishes_bottom_nav.domain.api

import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.domain.model.Product

interface DishesRepository {
    fun createNewDish(dish: Dish)
    fun changeDish(dishForChange: Dish, newDish: Dish)
    fun toggleFavorite(dish: Dish)
    fun deleteDish(dish: Dish)
    fun getAllDishes(): List<Dish>
    fun getDishByName(dishName: String): Dish
    fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean
    fun getAvailableDishes(): List<Dish>
    fun toggleFavoriteProduct(product: Product)
    fun toggleBuyProduct(product: Product)
}