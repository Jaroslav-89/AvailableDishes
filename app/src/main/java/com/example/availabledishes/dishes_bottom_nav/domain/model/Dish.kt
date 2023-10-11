package com.example.availabledishes.dishes_bottom_nav.domain.model

import com.example.availabledishes.products_bottom_nav.domain.model.Product

data  class Dish (
    val name: String,
    val description: String,
    val recipe: String,
    val ingredients: List<Product>
)
