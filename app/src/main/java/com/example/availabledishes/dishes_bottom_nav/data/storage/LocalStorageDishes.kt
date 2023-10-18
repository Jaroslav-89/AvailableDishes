package com.example.availabledishes.dishes_bottom_nav.data.storage

import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish

interface LocalStorageDishes{
    fun getAllDishes(): List<Dish>
    fun getAvailableDishes(): List<Dish>
    fun changeDish(dish: Dish, newDish: Dish)
    fun getDishByName(dishName: String): Dish
    fun deleteDish(dish: Dish)
    fun createNewDish(dish: Dish)
    fun toggleFavorite(dish: Dish)
    fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean
}