package com.jaroapps.availabledishes.products_bottom_nav.data.convertors

import com.jaroapps.availabledishes.products_bottom_nav.data.entity.ProductListEntity
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList

object ProductListDbConvertor {
    fun map(productList: ProductList): ProductListEntity {
        return ProductListEntity(
            id = productList.id,
            name = productList.name,
            imgUrl = productList.imgUrl,
            description = productList.description,
            numberOfProducts = productList.numberOfProducts,
            numberOfPersons = productList.numberOfPersons,
            createData = productList.createData,
            lastEditDate = productList.lastEditDate,
        )
    }

    fun map(productList: ProductListEntity): ProductList {
        return ProductList(
            id = productList.id,
            name = productList.name,
            imgUrl = productList.imgUrl,
            description = productList.description,
            numberOfProducts = productList.numberOfProducts,
            numberOfPersons = productList.numberOfPersons,
            createData = productList.createData,
            lastEditDate = productList.lastEditDate,
        )
    }

    fun mapList(productLists: List<ProductListEntity>): List<ProductList> {
        val result = mutableListOf<ProductList>()
        productLists.forEach { productList ->
            result.add(map(productList))
        }
        return result
    }
}