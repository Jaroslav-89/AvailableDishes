package com.example.availabledishes.app

import android.app.Application
import android.content.SharedPreferences
import com.example.availabledishes.common.di.dataModule
import com.example.availabledishes.common.di.interactorModule
import com.example.availabledishes.common.di.repositoryModule
import com.example.availabledishes.common.di.viewModelModule
import com.example.availabledishes.dishes_bottom_nav.data.storage.AllDishes
import com.example.availabledishes.products_bottom_nav.data.storage.AllProducts
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    companion object {
        const val SHARED_PREFERENCES = "shared_preference"
        const val FIRST_LAUNCH = "first_launch"
        const val ALL_PRODUCTS = "all_products"
        const val ALL_DISHES = "all_dishes"
        lateinit var sharedPrefs: SharedPreferences
    }

    private var firstLaunch = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        firstLaunch = sharedPrefs.getBoolean(FIRST_LAUNCH, true)

        if (firstLaunch) {

            val allProductsListToJson = Gson().toJson(AllProducts().allProducts)
            val allDishesListToJson = Gson().toJson(AllDishes().allDishes)

            sharedPrefs.edit()
                .putBoolean(FIRST_LAUNCH, false)
                .putString(ALL_DISHES, allDishesListToJson)
                .putString(ALL_PRODUCTS, allProductsListToJson)
                .apply()
        }
    }
}