package com.example.availabledishes.products_bottom_nav.data.storage

import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

open class AllProductsTag {
    open val allTags = listOf<ProductTag>(
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
        ),
        ProductTag(
            name = "молочные",
            color = "#ED9434"
        ),
        ProductTag(
            name = "бакалея",
            color = "#7593DF"
        ),
        ProductTag(
                name = "сладкое",
            color = "#82EF7A"
        ),
        ProductTag(
            name = "специи",
            color = "#41BA46"
        ),
        ProductTag(
            name = "рыба",
            color = "#FFFFFFFF"
        ),
        ProductTag(
            name = "птица",
            color = "#7593DF"
        ),
        ProductTag(
            name = "ягоды",
            color = "#F68299"
        ),
        ProductTag(
            name = "орехи",
            color = "#ED9434"
        ),
    )
}