package com.example.availabledishes.products_bottom_nav.domain.model

data class Product (
    val name: String,
    val imgUrl: String?,
    val tag: List<Tags>?,
    val description: String?,
    val inFavorite: Boolean?,
    val needToBuy: Boolean?,
)