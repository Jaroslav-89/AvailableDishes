package com.example.availabledishes.products_bottom_nav.data.storage.shared_prefs

import android.content.SharedPreferences
import com.example.availabledishes.dishes_bottom_nav.data.storage.shared_prefs.LocalStorageDishesImpl
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.data.storage.LocalStorageProducts
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag
import com.google.gson.Gson

class LocalStorageProductsImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    LocalStorageProducts {
    companion object {
        const val ALL_PRODUCTS = "all_products"
    }

    override fun getAllProductsList(): List<Product> {
        return productsListFromJson(sharedPreferences.getString(ALL_PRODUCTS, ""))
    }

    override fun getMyProductsList(): List<Product> {
        val allProductsList = getAllProductsList().toMutableList()
        val myProductList = mutableListOf<Product>()
        for (product in allProductsList) {
            if (product.inFavorite == true) {
                myProductList.add(product)
            }
        }
        return myProductList
    }

    override fun getBuyProductsList(): List<Product> {
        val allProductsList = getAllProductsList().toMutableList()
        val buyProductList = mutableListOf<Product>()
        for (product in allProductsList) {
            if (product.inFavorite == true && product.needToBuy == true) {
                buyProductList.add(product)
            }
        }
        return buyProductList
    }

    override fun getProductByName(productName: String): Product {
        val allProductsList = getAllProductsList().toMutableList()
        for (product in allProductsList) {
            if (product.name == productName) {
                return product
            }
        }
        return Product("", null, listOf<ProductTag>(), null, null, null)
    }

    override fun getAllDishesWithThisProduct(product: Product): List<Dish> {
        val allDishesList =
            dishesListFromJson(sharedPreferences.getString(LocalStorageDishesImpl.ALL_DISHES, ""))
        val availableDishes = mutableListOf<Dish>()
        for (dish in allDishesList) {
            if (dish.ingredients?.contains(product) == true) {
                availableDishes.add(dish)
            }
        }
        return availableDishes
    }

    override fun deleteProduct(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()
        if (allProductsList.contains(product)) {
            allProductsList.remove(product)
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun createNewProduct(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()
        if (!allProductsList.contains(product)) {
            allProductsList.add(product)
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun changeProduct(productForChange: Product, newProduct: Product) {
        val allProductsList = getAllProductsList().toMutableList()
        for (product in allProductsList) {
            if (product.name == productForChange.name) {
                allProductsList.remove(product)
                allProductsList.add(newProduct)
                sharedPreferences.edit()
                    .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
                    .apply()
                return
            }
        }
        allProductsList.add(Product("", null, mutableListOf<ProductTag>(), null, null, null))
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun toggleFavorite(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()

        if (product.inFavorite == null || !product.inFavorite!!) {
            allProductsList.remove(product)
            allProductsList.add(product.copy(inFavorite = true))
        } else {
            allProductsList.remove(product)
            allProductsList.add(product.copy(inFavorite = false, needToBuy = false))
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun toggleBuy(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()

        if (product.needToBuy == null || !product.needToBuy!!) {
            allProductsList.remove(product)
            allProductsList.add(product.copy(inFavorite = true, needToBuy = true))
        } else {
            allProductsList.remove(product)
            allProductsList.add(product.copy(needToBuy = false))
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun toggleDishFavorite(dish: Dish) {
        val allDishesList =
            dishesListFromJson(
                sharedPreferences.getString(
                    LocalStorageDishesImpl.ALL_DISHES,
                    ""
                )
            ).toMutableList()
        if (allDishesList.contains(dish)) {
            allDishesList.remove(dish)
            if (dish.inFavorite == true) {
                allDishesList.add(dish.copy(inFavorite = false))
            } else {
                allDishesList.add(dish.copy(inFavorite = true))
            }
        }
        sharedPreferences.edit()
            .putString(LocalStorageDishesImpl.ALL_DISHES, dishesListToJson(allDishesList))
            .apply()
    }

    override fun checkingNameNewProductForMatches(newNameForCheck: String): Boolean {
        val allProductsList = getAllProductsList().toMutableList()
        for (product in allProductsList) {
            if (product.name.lowercase().trim() == newNameForCheck.lowercase().trim()) {
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
}