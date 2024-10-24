package com.jaroapps.availabledishes.common.data.convertors

import com.jaroapps.availabledishes.products_bottom_nav.data.entity.ProductEntity
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product

object ProductDbConvertor {
    fun map(product: Product): ProductEntity {
        return ProductEntity(
            id = product.id,
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
            id = product.id,
            name = product.name,
            imgUrl = product.imgUrl,
            tag = mapStringToList(product.tag),
            description = product.description,
            inFavorite = product.inFavorite,
            needToBuy = product.needToBuy,
            dishes = mapStringToList(product.dishes)
        )
    }

    fun mapList(productList: List<ProductEntity>): List<Product> {
        val result = mutableListOf<Product>()
        productList.forEach { product ->
            result.add(map(product))
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