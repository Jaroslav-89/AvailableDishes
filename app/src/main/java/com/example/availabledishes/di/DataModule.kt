package com.example.availabledishes.di

import android.content.Context
import com.example.availabledishes.app.App
import com.example.availabledishes.dishes_bottom_nav.data.storage.LocalStorageDishes
import com.example.availabledishes.dishes_bottom_nav.data.storage.shared_prefs.LocalStorageDishesImpl
import com.example.availabledishes.products_bottom_nav.data.storage.LocalStorageProducts
import com.example.availabledishes.products_bottom_nav.data.storage.shared_prefs.LocalStorageProductsImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    factory {
        Gson()
    }

    single {
        androidContext().getSharedPreferences(
            App.SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single<LocalStorageProducts> {
        LocalStorageProductsImpl(
            sharedPreferences = get(),
            gson = get()
        )
    }

    single<LocalStorageDishes> {
        LocalStorageDishesImpl(
            sharedPreferences = get(),
            gson = get()
        )
    }
}