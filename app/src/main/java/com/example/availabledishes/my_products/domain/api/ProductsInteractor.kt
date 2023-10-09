package com.example.availabledishes.my_products.domain.api

import com.example.availabledishes.my_products.domain.model.Product

interface ProductsInteractor {

    fun getProductsList(expression: String, consumer: ProductsConsumer)

    interface ProductsConsumer {
        fun consume(foundMovies: List<Product>?)
    }
}