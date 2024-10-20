package com.jaroapps.availabledishes.notes_bottom_nav.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.RefrigeratorEntity

@Dao
interface RefrigeratorsDao {
    @Upsert(entity = RefrigeratorEntity::class)
    suspend fun upsertRefrigerator(refrigeratorEntity: RefrigeratorEntity)

    @Query("SELECT * FROM refrigerator_table WHERE name = :name LIMIT 1")
    suspend fun getRefrigeratorByName(name: String): RefrigeratorEntity

    @Query("SELECT * FROM refrigerator_table")
    suspend fun getAllRefrigerator(): List<RefrigeratorEntity>

    @Query("DELETE FROM refrigerator_table WHERE name = :name")
    suspend fun deleteRefrigerator(name: String)
}