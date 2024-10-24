package com.jaroapps.availabledishes.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.availabledishes.products_bottom_nav.data.entity.ProductListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductListDao {
    @Upsert(entity = ProductListEntity::class)
    suspend fun upsertProductList(productListEntity: ProductListEntity)

    @Query("SELECT * FROM product_list_table")
    fun getAllProductLists(): Flow<List<ProductListEntity>>

    @Query("SELECT * FROM product_list_table WHERE id = :id LIMIT 1")
    suspend fun getProductListById(id: String): ProductListEntity

    @Query("DELETE FROM product_list_table WHERE id = :id")
    suspend fun deleteProductList(id: String)
}