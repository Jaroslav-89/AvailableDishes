package com.example.availabledishes.dishes_bottom_nav.data

import com.example.availabledishes.dishes_bottom_nav.data.storage.LocalStorageDishes
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish

class DishesRepositoryImpl(private val localStorage: LocalStorageDishes) : DishesRepository {
    override fun getAllDishes(): List<Dish> {
        return localStorage.getAllDishes()
    }

    override fun getAvailableDishes(): List<Dish> {
        return localStorage.getAvailableDishes()
    }

    override fun changeDish(dish: Dish, newDish: Dish) {
        TODO("Not yet implemented")
    }

    override fun getDishByName(dishName: String): Dish {
        TODO("Not yet implemented")
    }

    override fun deleteDish(dish: Dish) {
        TODO("Not yet implemented")
    }

    override fun createNewDish(dish: Dish) {
        TODO("Not yet implemented")
    }

    override fun toggleFavorite(dish: Dish) {
        TODO("Not yet implemented")
    }

    override fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean {
        TODO("Not yet implemented")
    }
}