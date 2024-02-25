package com.jaroapps.availabledishes.app

import android.app.Application
import android.content.SharedPreferences
import com.jaroapps.availabledishes.common.di.dataModule
import com.jaroapps.availabledishes.common.di.interactorModule
import com.jaroapps.availabledishes.common.di.repositoryModule
import com.jaroapps.availabledishes.common.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    private var firstLaunch = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
//
//        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
//
//        firstLaunch = sharedPrefs.getBoolean(FIRST_LAUNCH, true)
//
//        if (firstLaunch) {
//
//           // val allProductsListToJson = Gson().toJson(AllProducts().allProducts)
//            //val allDishesListToJson = Gson().toJson(AllDishes().allDishes)
//
//            sharedPrefs.edit()
//                .putBoolean(FIRST_LAUNCH, false)
//                .putString(ALL_DISHES, allDishesListToJson)
//                .putString(ALL_PRODUCTS, allProductsListToJson)
//                .apply()
//        }
    }

    companion object {
        const val SHARED_PREFERENCES = "shared_preference"
        const val FIRST_LAUNCH = "first_launch"
        const val ALL_PRODUCTS = "all_products"
        const val ALL_DISHES = "all_dishes"
        lateinit var sharedPrefs: SharedPreferences
    }
}