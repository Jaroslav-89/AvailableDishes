package com.example.availabledishes.common.data.convertors

import com.example.availabledishes.products_bottom_nav.data.entity.ProductEntity
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class ProductDbConvertor {
    fun map(product: Product): ProductEntity {
        return ProductEntity(
            name = product.name,
            imgUrl = product.imgUrl,
            tag = product.tag.joinToString(","),
            description = product.description,
            inFavorite = product.inFavorite,
            needToBuy = product.needToBuy,
            dishes = product.dishes.joinToString(",")
        )
    }

    fun map(product: ProductEntity): Product {
        return Product(
            name = product.name,
            imgUrl = product.imgUrl,
            tag = mapStringToList(product.tag),
            description = product.description,
            inFavorite = product.inFavorite,
            needToBuy = product.needToBuy,
            dishes = mapStringToList(product.dishes)
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