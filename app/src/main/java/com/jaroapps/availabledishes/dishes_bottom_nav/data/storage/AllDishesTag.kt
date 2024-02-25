package com.jaroapps.availabledishes.dishes_bottom_nav.data.storage

import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.DishTag

open class AllDishesTag {
    open val allDishesTags = listOf<DishTag>(
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
        ),
        DishTag(
            name = "сладкое",
            color = "#82EF7A"
        ),
        DishTag(
            name = "специи",
            color = "#41BA46"
        ),
        DishTag(
            name = "рыба",
            color = "#FFFFFFFF"
        ),
        DishTag(
            name = "птица",
            color = "#7593DF"
        ),
        DishTag(
            name = "ягоды",
            color = "#F68299"
        ),
        DishTag(
            name = "орехи",
            color = "#ED9434"
        ),
    )
}