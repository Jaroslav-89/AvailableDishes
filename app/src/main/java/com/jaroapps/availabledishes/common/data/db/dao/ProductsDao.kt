package com.jaroapps.availabledishes.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jaroapps.availabledishes.products_bottom_nav.data.entity.ProductEntity

@Dao
interface ProductsDao {
    @Upsert(entity = ProductEntity::class)
    suspend fun upsertProduct(productEntity: ProductEntity)

    @Transaction
    suspend fun changeProductWitsName(productForDelete: String, newProduct: ProductEntity) {
        deleteProduct(productForDelete)
        upsertProduct(newProduct)
    }

    @Query("SELECT * FROM product_table")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM product_table WHERE name = :name LIMIT 1")
    suspend fun getProductByName(name: String): ProductEntity

    @Query("DELETE FROM product_table WHERE name = :name")
    suspend fun deleteProduct(name: String)

    @Query("SELECT * FROM product_table WHERE inFavorite = 1")
    suspend fun getMyProducts(): List<ProductEntity>

    @Query("SELECT * FROM product_table WHERE needToBuy = 1")
    suspend fun getBuyProducts(): List<ProductEntity>

    @Query("DELETE FROM product_table WHERE name LIKE :query LIMIT 5")
    suspend fun getQueryProducts(query: String): List<ProductEntity>
}