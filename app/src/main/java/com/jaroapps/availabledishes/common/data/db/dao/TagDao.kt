package com.jaroapps.availabledishes.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.availabledishes.common.data.db.entity.TagEntity

@Dao
interface TagDao {
    @Upsert(entity = TagEntity::class)
    suspend fun upsertTag(tagEntity: TagEntity)

    @Query("SELECT * FROM tag_table WHERE type = :type")
    suspend fun getAllProductTag(type: String = PRODUCT): List<TagEntity>

    @Query("SELECT * FROM tag_table WHERE name = :name AND type = :type LIMIT 1")
    suspend fun getProductTagByName(name: String, type: String = PRODUCT): TagEntity

    @Query("DELETE FROM tag_table WHERE name = :name AND type = :type")
    suspend fun deleteProductTagByName(name: String, type: String = PRODUCT)

    @Query("SELECT * FROM tag_table WHERE type = :type")
    suspend fun getAllDishTag(type: String = DISH): List<TagEntity>

    @Query("SELECT * FROM tag_table WHERE name = :name AND type = :type LIMIT 1")
    suspend fun getDishTagByName(name: String, type: String = DISH): TagEntity

    @Query("DELETE FROM tag_table WHERE name = :name AND type = :type")
    suspend fun deleteDishTagByName(name: String, type: String = DISH)

    companion object {
        private const val PRODUCT = "product_tag"
        private const val DISH = "dish_tag"
    }
}