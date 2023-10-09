package com.example.availabledishes.my_products.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson

class LocalStorage(private val sharedPreferences: SharedPreferences, private val gson: Gson) {
    private companion object {
        const val FAVORITES_KEY = "FAVORITES_KEY"
    }
}