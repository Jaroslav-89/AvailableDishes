package com.example.availabledishes.products_bottom_nav.data.storage

import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

class AllProducts {
    val allProducts = listOf<Product>(
        Product(
            name = "Яйца куриные",
            imgUrl = null,
            tag = mutableListOf<ProductTag>(
                ProductTag(
                    name = "мясо",
                    color = "#E6E8EB"
                ),
                ProductTag(
                    name = "овощи",
                    color = "#88FF00"
                ),
                ProductTag(
                    name = "фрукты",
                    color = "#F68299"
                )),
            description = null,
            inFavorite = null,
            needToBuy = null
        ),
        Product(
            name = "Картофель",
            imgUrl = null,
            tag = null,
            description = null,
            inFavorite = null,
            needToBuy = null
        ),
        Product(
            name = "Молоко",
            imgUrl = null,
            tag = null,
            description = null,
            inFavorite = null,
            needToBuy = null
        ),
        Product(
            name = "Овсянка",
            imgUrl = null,
            tag = mutableListOf<ProductTag>(
                ProductTag(
                    name = "мясо",
                    color = "#E6E8EB"
                ),
                ProductTag(
                    name = "овощи",
                    color = "#88FF00"
                ),
                ProductTag(
                    name = "фрукты",
                    color = "#F68299"
                )),
            description = null,
            inFavorite = null,
            needToBuy = null
        ),
        Product(
            name = "Картоха",
            imgUrl = null,
            tag = mutableListOf(ProductTag("тег","#F68299")),
            description = null,
            inFavorite = null,
            needToBuy = null
        )
    )
}
