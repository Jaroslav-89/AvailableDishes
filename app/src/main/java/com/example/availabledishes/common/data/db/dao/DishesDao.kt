package com.example.availabledishes.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.availabledishes.dishes_bottom_nav.data.entity.DishEntity

@Dao
interface DishesDao {
    @Upsert(entity = DishEntity::class)
    suspend fun upsertDish(dishEntity: DishEntity)

    @Query("SELECT * FROM dish_table")
    suspend fun getAllDish(): List<DishEntity>

    @Query("SELECT * FROM dish_table WHERE name = :name")
    suspend fun getDishByName(name: String): DishEntity

    @Query("DELETE FROM dish_table WHERE name = :name")
    suspend fun deleteDish(name: String)
}