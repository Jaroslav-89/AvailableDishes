package com.example.availabledishes.products_bottom_nav.data.storage

import com.example.availabledishes.products_bottom_nav.domain.model.Product

class AllProducts {
    val allProducts = listOf<Product>(
        Product("Яйца куриные",null, null, null),
        Product("Картофель", null, null, null),
        Product("Молоко", null, null, null),
        Product("Овсянка", null, null, null),
    )
}