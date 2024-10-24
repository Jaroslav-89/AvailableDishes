package com.jaroapps.availabledishes.common.data.convertors

import com.jaroapps.availabledishes.dishes_bottom_nav.data.entity.DishEntity
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish

object DishDbConvertor {
    fun map(dish: Dish): DishEntity {
        return DishEntity(
            id = dish.id,
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
            id = dish.id,
            name = dish.name,
            imgUrl = dish.imgUrl,
            tag = mapStringToList(dish.tag),
            description = dish.description,
            recipe = dish.recipe,
            ingredients = mapStringToList(dish.ingredients),
            inFavorite = dish.inFavorite,
        )
    }

    fun mapList(dishesList: List<DishEntity>): List<Dish> {
        val result = mutableListOf<Dish>()
        dishesList.forEach { dish ->
            result.add(map(dish))
        }
        return result
    }

    private fun mapStringToList(str: String): List<String> {
        return if (str.isEmpty()) {
            emptyList()
        } else {
            str.split(",")
        }
    }
}