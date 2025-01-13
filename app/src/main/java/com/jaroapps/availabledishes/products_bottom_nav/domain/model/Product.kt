package com.jaroapps.availabledishes.products_bottom_nav.domain.model

data class Product(
    val id: Int = 0,
    val name: String = "",
    val imgUrl: String = "",
    val tag: List<String> = emptyList(),
    val description: String = "",
    val productLists: List<String> = emptyList(),
    val dishes: List<String> = emptyList(),
)
