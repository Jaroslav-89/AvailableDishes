package com.jaroapps.availabledishes.products_bottom_nav.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_list_table")
data class ProductListEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imgUrl: String = "",
    val description: String = "",
    val numberOfProducts: Int = 0,
    val numberOfPersons: Int = 1,
    val createData: Long,
    val lastEditDate: Long,
)