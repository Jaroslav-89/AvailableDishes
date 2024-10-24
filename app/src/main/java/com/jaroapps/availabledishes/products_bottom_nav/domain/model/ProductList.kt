package com.jaroapps.availabledishes.products_bottom_nav.domain.model

data class ProductList(
    val id: String,
    val name: String,
    val imgUrl: String = "",
    val description: String = "",
    val numberOfProducts: Int = 0,
    val numberOfPersons: Int = 1,
    val createData: Long,
    val lastEditDate: Long,
)