package com.example.availabledishes.dishes_bottom_nav.domain.model

import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.domain.model.Tags

data  class Dish (
    val name: String,
    val imgUrl: String?,
    val tag: List<Tags>?,
    val description: String,
    val recipe: String,
    val ingredients: List<Product>,
    val inFavorite: Boolean?,
)
