package com.example.availabledishes.dishes_bottom_nav.data.storage.shared_prefs

import android.content.SharedPreferences
import com.example.availabledishes.dishes_bottom_nav.data.storage.LocalStorageDishes
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.dishes_bottom_nav.domain.model.DishTag
import com.example.availabledishes.products_bottom_nav.data.storage.shared_prefs.LocalStorageProductsImpl
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.google.gson.Gson

class LocalStorageDishesImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    LocalStorageDishes {

    companion object {
        const val ALL_DISHES = "all_dishes"
    }

    override fun getAllDishes(): List<Dish> {
        return dishesListFromJson(sharedPreferences.getString(ALL_DISHES, ""))
    }

    private fun getAllProductsList(): List<Product> {
        return productsListFromJson(
            sharedPreferences.getString(
                LocalStorageProductsImpl.ALL_PRODUCTS,
                ""
            )
        )
    }

    override fun getAvailableDishes(): List<Dish> {
        val allDishesList = getAllDishes().toMutableList()
        val allProductsList = getAllProductsList().toMutableList()
        val availableProductsList = mutableListOf<Product>()
        val availableDish = mutableListOf<Dish>()

        for (product in allProductsList) {
            if (product.inFavorite == true && product.needToBuy == false) {
                availableProductsList.add(product)
            }
        }

        for (dish in allDishesList) {
            val dishCollection: Collection<Product> =
                dish.ingredients?.toCollection() ?: emptyList()
            if (availableProductsList.containsAll(dishCollection)) {
                availableDish.add(dish)
            }
        }
        return availableDish
    }


    override fun changeDish(dish: Dish, newDish: Dish) {
        TODO("Not yet implemented")
    }

    override fun getDishByName(dishName: String): Dish {
        val allDishesList = getAllDishes().toMutableList()
        for (dish in allDishesList) {
            if (dish.name == dishName) {
                return dish
            }
        }
        return Dish("", null, emptyList<DishTag>(), null, null, emptyList<Product>(), null)
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

    private fun dishesListFromJson(json: String?): List<Dish> {
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        return gson.fromJson(json, Array<Dish>::class.java).toList()
    }

    private fun productsListFromJson(json: String?): List<Product> {
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        return gson.fromJson(json, Array<Product>::class.java).toList()
    }

    private fun dishesListToJson(dishesList: List<Dish>): String {
        return gson.toJson(dishesList)
    }
}

private fun <E> List<E>.toCollection(): Collection<E> {
    val result = mutableListOf<E>()
    result.addAll(this)
    return result
}
