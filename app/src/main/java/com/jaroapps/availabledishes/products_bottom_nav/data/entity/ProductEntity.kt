package com.jaroapps.availabledishes.products_bottom_nav.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val imgUrl: String,
    val tag: String,
    val description: String,
    val productLists: String,
    val dishes: String,
)
