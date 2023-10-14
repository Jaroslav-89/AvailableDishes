package com.example.availabledishes.dishes_bottom_nav.domain.model

import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

data class Dish(
    val name: String,
    val imgUrl: String?,
    val tag: List<ProductTag>?,
    val description: String?,
    val recipe: String?,
    val ingredients: List<String>?,
    val inFavorite: Boolean?,
)
