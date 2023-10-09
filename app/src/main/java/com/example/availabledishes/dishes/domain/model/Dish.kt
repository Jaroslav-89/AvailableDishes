package com.example.availabledishes.dishes.domain.model

import com.example.availabledishes.my_products.domain.model.Product

data  class Dish (
    val name: String,
    val description: String,
    val recipe: String,
    val ingredients: List<Product>
)
