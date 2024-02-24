package com.example.availabledishes.products_bottom_nav.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val imgUrl: String,
    val tag: String,
    val description: String,
    val inFavorite: Boolean,
    val needToBuy: Boolean,
    val dishes: String,
)
