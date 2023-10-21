package com.example.availabledishes.dishes_bottom_nav.domain.api

import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.domain.model.Product

interface DishesInteractor {
    fun getAllDishes(): List<Dish>
    fun getAvailableDishes(): List<Dish>
    fun changeDish(dishForChange: Dish, newDish: Dish)
    fun getDishByName(dishName: String): Dish
    fun deleteDish(dish: Dish)
    fun createNewDish(dish: Dish)
    fun toggleFavorite(dish: Dish)
    fun toggleFavoriteProduct(product: Product)
    fun toggleBuyProduct(product: Product)
    fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean
}