package com.example.availabledishes.products_bottom_nav.data.storage.shared_prefs

import android.content.SharedPreferences
import com.example.availabledishes.products_bottom_nav.data.storage.LocalStorage
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.google.gson.Gson

class LocalStorageImpl(private val sharedPreferences: SharedPreferences, private val gson: Gson) :
    LocalStorage {
    companion object {
        const val ALL_PRODUCTS = "all_products"
        const val FAVORITES_PRODUCTS = "favorites_products"
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
        return Product("", null, null, null)
    }

    override fun deleteProduct(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()
        if (allProductsList.contains(product)){
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

    override fun changeProduct(product: Product, changeProduct: Product) {
        val allProductsList = getAllProductsList().toMutableList()
        if (allProductsList.contains(product)) {
            allProductsList.remove(product)
            allProductsList.add(changeProduct)
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

    override fun toggleFavorite(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()

        if (product.inFavorite == null || !product.inFavorite) {
            allProductsList.remove(product)
            allProductsList.add(product.copy(inFavorite = true))
        } else {
            allProductsList.remove(product)
            allProductsList.add(product.copy(inFavorite = false))
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

//     fun toggleFavoriteX(product: Product) {
//        val allProductsList = getAllProductsList().toMutableList()
//        val myProductsList = getFavoritesProducts().toMutableList()
//        if (product.inFavorite) {
//            if (allProductsList.contains(product)) {
//                allProductsList.remove(product)
//                allProductsList.add(product.copy(inFavorite = false))
//                if (myProductsList.contains(product)) {
//                    myProductsList.remove(product)
//                }
//            }
//        } else {
//            allProductsList.remove(product)
//            allProductsList.add(product.copy(inFavorite = true))
//            if (!myProductsList.contains(product)) {
//                myProductsList.add(product.copy(inFavorite = true))
//            }
//        }
//
//        sharedPreferences.edit()
//            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
//            .putString(FAVORITES_PRODUCTS, productsListToJson(myProductsList))
//            .apply()
//    }

    override fun toggleBuy(product: Product) {
        val allProductsList = getAllProductsList().toMutableList()

        if (product.needToBuy == null || !product.needToBuy) {
            allProductsList.remove(product)
            allProductsList.add(product.copy(needToBuy = true))
        } else {
            allProductsList.remove(product)
            allProductsList.add(product.copy(needToBuy = false))
        }
        sharedPreferences.edit()
            .putString(ALL_PRODUCTS, productsListToJson(allProductsList))
            .apply()
    }

//    private fun getFavoritesProducts(): List<Product> {
//        return productsListFromJson(sharedPreferences.getString(FAVORITES_PRODUCTS, ""))
//    }

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