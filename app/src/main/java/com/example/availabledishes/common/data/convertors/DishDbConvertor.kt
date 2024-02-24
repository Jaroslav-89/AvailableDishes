package com.example.availabledishes.common.data.convertors

import com.example.availabledishes.dishes_bottom_nav.data.entity.DishEntity
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish

class DishDbConvertor {
    fun map(dish: Dish): DishEntity {
        return DishEntity(
            name = dish.name,
            imgUrl = dish.imgUrl,
            tag = dish.tag.joinToString(","),
            description = dish.description,
            recipe = dish.recipe,
            ingredients = dish.ingredients.joinToString(","),
            inFavorite = dish.inFavorite,
        )
    }

    fun map(dish: DishEntity): Dish {
        return Dish(
            name = dish.name,
            imgUrl = dish.imgUrl,
            tag = mapStringToList(dish.tag),
            description = dish.description,
            recipe = dish.recipe,
            ingredients = mapStringToList(dish.ingredients),
            inFavorite = dish.inFavorite,
        )
    }

    private fun mapStringToList(str: String): List<String> {
        return if (str.isEmpty()) {
            emptyList()
        } else {
            str.split(",")
        }
    }
}