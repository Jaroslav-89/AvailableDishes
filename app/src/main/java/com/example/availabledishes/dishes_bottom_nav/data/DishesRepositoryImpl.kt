package com.example.availabledishes.dishes_bottom_nav.data

import com.example.availabledishes.dishes_bottom_nav.data.storage.LocalStorageDishes
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class DishesRepositoryImpl(private val localStorage: LocalStorageDishes) : DishesRepository {
    override fun getAllDishes(): List<Dish> {
        return localStorage.getAllDishes()
    }

    override fun getAvailableDishes(): List<Dish> {
        return localStorage.getAvailableDishes()
    }

    override fun changeDish(dishForChange: Dish, newDish: Dish) {
        localStorage.changeDish(dishForChange, newDish)
    }

    override fun getDishByName(dishName: String): Dish {
        return localStorage.getDishByName(dishName)
    }

    override fun deleteDish(dish: Dish) {
        localStorage.deleteDish(dish)
    }

    override fun createNewDish(dish: Dish) {
        localStorage.createNewDish(dish)
    }

    override fun toggleFavorite(dish: Dish) {
        localStorage.toggleFavorite(dish)
    }

    override fun toggleFavoriteProduct(product: Product) {
        localStorage.toggleFavoriteProduct(product)
    }

    override fun toggleBuyProduct(product: Product) {
        localStorage.toggleBuyProduct(product)
    }

    override fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean {
        return localStorage.checkingNameNewDishForMatches(newNameForCheck)
    }
}