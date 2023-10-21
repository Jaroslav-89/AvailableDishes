package com.example.availabledishes.dishes_bottom_nav.domain.impl

import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class DishesInteractorImpl(private val repository: DishesRepository) : DishesInteractor {
    override fun getAllDishes(): List<Dish> {
        return repository.getAllDishes()
    }

    override fun getAvailableDishes(): List<Dish> {
        return repository.getAvailableDishes()
    }

    override fun changeDish(dishForChange: Dish, newDish: Dish) {
        repository.changeDish(dishForChange, newDish)
    }

    override fun getDishByName(dishName: String): Dish {
        return repository.getDishByName(dishName)
    }

    override fun deleteDish(dish: Dish) {
        repository.deleteDish(dish)
    }

    override fun createNewDish(dish: Dish) {
        repository.createNewDish(dish)
    }

    override fun toggleFavorite(dish: Dish) {
        repository.toggleFavorite(dish)
    }

    override fun toggleFavoriteProduct(product: Product) {
        repository.toggleFavoriteProduct(product)
    }

    override fun toggleBuyProduct(product: Product) {
        repository.toggleBuyProduct(product)
    }

    override fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean {
        return repository.checkingNameNewDishForMatches(newNameForCheck)
    }
}