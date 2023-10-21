package com.example.availabledishes.dishes_bottom_nav.data.storage.shared_prefs

import android.content.SharedPreferences
import android.util.Log
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
        Log.d("XXX", "олучаем список доступных блюд")
        val allDishesList =
            dishesListFromJson(sharedPreferences.getString(ALL_DISHES, "")).toMutableList()
        val allProductsList = getAllProductsList().toMutableList()
        val availableProductsList = mutableListOf<Product>()
        val availableDishList = mutableListOf<Dish>()

        for (product in allProductsList) {
            if (product.inFavorite == true && product.needToBuy != true) {
                availableProductsList.add(product)
                Log.d("XXX", "записали продукт")
                Log.d("XXX", availableProductsList.toString())
            }
        }

        for (dish in allDishesList) {
            if (!dish.ingredients.isNullOrEmpty()){
                var count = 0
                for (ingredient in dish.ingredients){
                    for (product in availableProductsList){
                        if(product.name == ingredient.name){
                            count++
                        }
                    }
                }
                if (dish.ingredients.size == count){
                    availableDishList.add(dish)
                }
            }
        }
        return availableDishList

//        for (dish in allDishesList) {
//            val dishCollection: Collection<Product> =
//                dish.ingredients?.toCollection() ?: emptyList()
//            if (availableProductsList.containsAll(dishCollection)) {
//                availableDish.add(dish)
//            }
//        }
//        return availableDish
    }

    override fun changeDish(dishForChange: Dish, newDish: Dish) {
        val allDishesList = getAllDishes().toMutableList()
        for (dish in allDishesList) {
            if (dish.name == dishForChange.name) {
                allDishesList.remove(dish)
                allDishesList.add(newDish)
                sharedPreferences.edit()
                    .putString(ALL_DISHES, dishesListToJson(allDishesList))
                    .apply()
                return
            }
        }
        allDishesList.add(
            Dish(
                "",
                null,
                emptyList<DishTag>(),
                null,
                null,
                emptyList<Product>(),
                null
            )
        )
        sharedPreferences.edit()
            .putString(ALL_DISHES, dishesListToJson(allDishesList))
            .apply()
    }

    override fun getDishByName(dishName: String): Dish {
        val allDishesList = getAllDishes().toMutableList()
        val allProductsList = getAllProductsList().toMutableList()
        val newIngredientsList = mutableListOf<Product>()
        var myDish: Dish? = null
        var newDish: Dish? = null

        for (dish in allDishesList) {
            if (dish.name == dishName) {
                myDish = dish.copy(
                    ingredients = dish.ingredients?.toMutableList()
                        ?.sortedBy { it.name.lowercase() }
                        ?.sortedByDescending { it.needToBuy == false || it.needToBuy == null })
            }
        }
        if (myDish!!.ingredients != null) {
            for (ingredient in myDish.ingredients!!) {
                for (product in allProductsList) {
                    if (ingredient.name == product.name) {
                        newIngredientsList.add(product)
                    }
                }
            }
            newDish = myDish.copy(ingredients = newIngredientsList)
            return newDish
        }
        return Dish("", null, emptyList<DishTag>(), null, null, emptyList<Product>(), null)
    }

    override fun deleteDish(dish: Dish) {
        val allDishesList = getAllDishes().toMutableList()
        if (allDishesList.contains(dish)) {
            allDishesList.remove(dish)
        }
        sharedPreferences.edit()
            .putString(ALL_DISHES, dishesListToJson(allDishesList))
            .apply()
    }

    override fun createNewDish(dish: Dish) {
        val allDishesList = getAllDishes().toMutableList()
        if (!allDishesList.contains(dish)) {
            allDishesList.add(dish)
        }
        sharedPreferences.edit()
            .putString(ALL_DISHES, dishesListToJson(allDishesList))
            .apply()
    }

    override fun toggleFavorite(dish: Dish) {
        val allDishesList = getAllDishes().toMutableList()
        val newAllDishList = allDishesList
        for (item in allDishesList) {
            if (item.name == dish.name) {
                newAllDishList.remove(item)
                if (dish.inFavorite == true) {
                    newAllDishList.add(dish.copy(inFavorite = false))
                    break
                } else {
                    newAllDishList.add(dish.copy(inFavorite = true))
                    break
                }
            }
        }
        sharedPreferences.edit()
            .putString(ALL_DISHES, dishesListToJson(allDishesList))
            .apply()
    }

    override fun toggleFavoriteProduct(product: Product) {
        val allDishesList = getAllDishes().toMutableList()
        val newAllDishList = mutableListOf<Dish>()
        for (dish in allDishesList) {
            var newProductsList = mutableListOf<Product>()
            if (dish.ingredients?.contains(product) == true) {
                newProductsList = dish.ingredients.toMutableList()
                newProductsList.remove(product)
                if (product.inFavorite == true) {
                    newProductsList.add(product.copy(inFavorite = false, needToBuy = false))
                } else {
                    newProductsList.add(product.copy(inFavorite = true, needToBuy = true))
                }
                newAllDishList.add(dish.copy(ingredients = newProductsList))
            } else {
                newAllDishList.add(dish)
            }
        }
        sharedPreferences.edit()
            .putString(ALL_DISHES, dishesListToJson(newAllDishList))
            .apply()

        val allProductsList = getAllProductsList().toMutableList()
        val newAllProductList = allProductsList.toMutableList()
        if (allProductsList.contains(product)) {
            newAllProductList.remove(product)
            if (product.inFavorite == true) {
                newAllProductList.add(product.copy(inFavorite = false, needToBuy = false))
            } else {
                newAllProductList.add(product.copy(inFavorite = true, needToBuy = true))
            }
        }
        sharedPreferences.edit()
            .putString(LocalStorageProductsImpl.ALL_PRODUCTS, productsListToJson(newAllProductList))
            .apply()
    }

    override fun toggleBuyProduct(product: Product) {
        val allDishesList = getAllDishes().toMutableList()
        for (dish in allDishesList) {
            if (dish.ingredients?.contains(product) == true) {
                if (product.needToBuy == null || !product.needToBuy) {
                    dish.ingredients.toMutableList().remove(product)
                    dish.ingredients.toMutableList()
                        .add(product.copy(inFavorite = true, needToBuy = true))
                } else {
                    dish.ingredients.toMutableList().remove(product)
                    dish.ingredients.toMutableList()
                        .add(product.copy(needToBuy = false))
                }
            }
        }
        sharedPreferences.edit()
            .putString(ALL_DISHES, dishesListToJson(allDishesList))
            .apply()

        val allProductsList = getAllProductsList().toMutableList()
        if (product.needToBuy == null || !product.needToBuy) {
            allProductsList.remove(product)
            allProductsList.add(product.copy(inFavorite = true, needToBuy = true))
        } else {
            allProductsList.remove(product)
            allProductsList.add(product.copy(needToBuy = false))
        }
        sharedPreferences.edit()
            .putString(LocalStorageProductsImpl.ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun checkingNameNewDishForMatches(newNameForCheck: String): Boolean {
        val allDishesList = getAllDishes().toMutableList()
        for (dish in allDishesList) {
            if (dish.name.lowercase().trim() == newNameForCheck.lowercase().trim()) {
                return false
            }
        }
        return true
    }

    private fun dishesListFromJson(json: String?): List<Dish> {
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        return gson.fromJson(json, Array<Dish>::class.java).toList()
    }

    private fun dishesListToJson(dishesList: List<Dish>): String {
        return gson.toJson(dishesList)
    }

    private fun productsListFromJson(json: String?): List<Product> {
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        return gson.fromJson(json, Array<Product>::class.java).toList()
    }

    private fun productsListToJson(productsList: List<Product>): String {
        return gson.toJson(productsList)
    }

    private fun <E> List<E>.toCollection(): Collection<E> {
        val result = mutableListOf<E>()
        result.addAll(this)
        return result
    }
}
