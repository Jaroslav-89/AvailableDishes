package com.example.availabledishes.dishes_bottom_nav.domain.impl

import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish

class DishesInteractorImpl(private val repository: DishesRepository) : DishesInteractor {
    override fun getAllDishes(): List<Dish> {
        return repository.getAllDishes()
    }

    override fun getAvailableDishes(): List<Dish> {
        return repository.getAvailableDishes()
    }

    override fun changeDish(dish: Dish, newDish: Dish) {
        TODO("Not yet implemented")
    }

    override fun getDishByName(dishName: String): Dish{
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