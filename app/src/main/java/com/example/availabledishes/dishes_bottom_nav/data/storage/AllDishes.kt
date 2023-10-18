package com.example.availabledishes.dishes_bottom_nav.data.storage

import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.dishes_bottom_nav.domain.model.DishTag
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

class AllDishes {
    val allDishes = listOf<Dish>(
        Dish("Салат", null, emptyList<DishTag>(), null, null, emptyList<Product>(), null),
        Dish("Суп", null, emptyList<DishTag>(), null, null, emptyList<Product>(), null),
        Dish("Щи", null, emptyList<DishTag>(), null, null, emptyList<Product>(), null),
        Dish("Жареха", null, emptyList<DishTag>(), null, null, emptyList<Product>(), null),
        Dish(
            name = "Окрошка",
            imgUrl = null,
            tag = listOf<DishTag>(
                DishTag(
                    name = "мясо",
                    color = "#E6E8EB"
                ),
                DishTag(
                    name = "овощи",
                    color = "#88FF00"
                ),
                DishTag(
                    name = "фрукты",
                    color = "#F68299"
                ),
                DishTag(
                    name = "молочные",
                    color = "#ED9434"
                ),
                DishTag(
                    name = "бакалея",
                    color = "#7593DF"
                )
            ),
            description = null,
            recipe = null,
            ingredients = listOf<Product>(
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
                        )
                    ),
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
                        )
                    ),
                    description = null,
                    inFavorite = null,
                    needToBuy = null
                ),
                Product(
                    name = "Картоха",
                    imgUrl = null,
                    tag = mutableListOf(ProductTag("тег", "#F68299")),
                    description = null,
                    inFavorite = null,
                    needToBuy = null
                )
            ),
            inFavorite = null
        )
    )
}