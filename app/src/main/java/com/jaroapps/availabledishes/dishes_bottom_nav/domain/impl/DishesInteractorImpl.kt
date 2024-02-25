package com.jaroapps.availabledishes.dishes_bottom_nav.domain.impl

import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.flow.Flow

class DishesInteractorImpl(private val repository: DishesRepository) : DishesInteractor {
    override suspend fun getAllDishes(): List<Dish> {
        return repository.getAllDishes()
    }

    override suspend fun getAvailableDishes(): List<Dish> {
        return repository.getAvailableDishes()
    }

    override suspend fun getQueryProducts(query: String): List<Product> {
        return repository.getQueryProducts(query)
    }

    override suspend fun changeDish(dishForChange: Dish, newDish: Dish) {
        repository.changeDish(dishForChange, newDish)
    }

    override suspend fun getDishByName(dishName: String): Dish {
        return repository.getDishByName(dishName)
    }

    override suspend fun getDishTagList(tags: List<String>): Flow<List<Tag>> {
        return repository.getDishTagList(tags)
    }

    override suspend fun getDishIngredients(products: List<String>): List<Product> {
        return repository.getDishIngredients(products)
    }

    override suspend fun getAllDishesTags(): List<Tag> {
        return repository.getAllDishesTags()
    }

    override suspend fun deleteDish(dish: Dish) {
        repository.deleteDish(dish)
    }

    override suspend fun createNewDish(dish: Dish) {
        repository.createNewDish(dish)
    }

    override suspend fun toggleFavorite(dish: Dish) {
        repository.toggleFavorite(dish)
    }

    override suspend fun toggleFavoriteProduct(product: Product) {
        repository.toggleFavoriteProduct(product)
    }

    override suspend fun toggleBuyProduct(product: Product) {
        repository.toggleBuyProduct(product)
    }

    override suspend fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean {
        return repository.checkingNameNewDishForMatches(newNameForCheck)
    }
}