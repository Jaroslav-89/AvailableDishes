package com.example.availabledishes.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.availabledishes.products_bottom_nav.data.entity.ProductEntity

@Dao
interface ProductsDao {
    @Upsert(entity = ProductEntity::class)
    suspend fun upsertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM product_table")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM product_table WHERE name = :name")
    suspend fun getProductByName(name: String): ProductEntity

    @Query("DELETE FROM product_table WHERE name = :name")
    suspend fun deleteProduct(name: String)

    @Query("SELECT * FROM product_table WHERE inFavorite = :inFavorite")
    suspend fun getMyProducts(inFavorite: Boolean): List<ProductEntity>

    @Query("SELECT * FROM product_table WHERE needToBuy = :needToBuy")
    suspend fun getBuyProducts(needToBuy: Boolean): List<ProductEntity>
}