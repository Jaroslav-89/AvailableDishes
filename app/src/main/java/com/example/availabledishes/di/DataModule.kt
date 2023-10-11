package com.example.availabledishes.di

import android.content.Context
import com.example.availabledishes.app.App
import com.example.availabledishes.products_bottom_nav.data.storage.LocalStorage
import com.example.availabledishes.products_bottom_nav.data.storage.shared_prefs.LocalStorageImpl
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

    single<LocalStorage> {
        LocalStorageImpl(
            sharedPreferences = get(),
            gson = get()
        )
    }
}