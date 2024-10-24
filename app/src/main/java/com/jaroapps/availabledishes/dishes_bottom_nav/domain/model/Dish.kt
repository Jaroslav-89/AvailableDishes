package com.jaroapps.availabledishes.dishes_bottom_nav.domain.model

data class Dish(
    val id: String = "",
    val name: String = "",
    val imgUrl: String = "",
    val tag: List<String> = emptyList(),
    val description: String = "",
    val recipe: String = "",
    val ingredients: List<String> = emptyList(),
    val inFavorite: Boolean = false,
)
