package com.example.availabledishes.my_products.domain.api

import com.example.availabledishes.my_products.domain.model.Product

interface ProductsRepository {
    fun getProductsList(expression: String): List<Product>
}