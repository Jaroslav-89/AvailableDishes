package com.example.availabledishes.products_bottom_nav.domain.model

data class Product(
    val name: String,
    val imgUrl: String = "",
    val tag: List<String> = emptyList(),
    val description: String = "",
    val inFavorite: Boolean = false,
    val needToBuy: Boolean = false,
    val dishes: List<String> = emptyList(),
)