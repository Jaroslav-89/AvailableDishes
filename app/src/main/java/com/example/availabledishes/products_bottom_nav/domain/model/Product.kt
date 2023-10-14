package com.example.availabledishes.products_bottom_nav.domain.model

data class Product(
    var name: String,
    var imgUrl: String?,
    var tag: MutableList<ProductTag>?,
    var description: String?,
    var inFavorite: Boolean?,
    var needToBuy: Boolean?,
)