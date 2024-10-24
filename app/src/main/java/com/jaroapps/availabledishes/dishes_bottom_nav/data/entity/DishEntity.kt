package com.jaroapps.availabledishes.dishes_bottom_nav.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish_table")
data class DishEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imgUrl: String,
    val tag: String,
    val description: String,
    val recipe: String,
    val ingredients: String,
    val inFavorite: Boolean,
)
