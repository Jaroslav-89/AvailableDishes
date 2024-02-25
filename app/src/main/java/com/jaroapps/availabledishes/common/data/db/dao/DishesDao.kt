package com.jaroapps.availabledishes.common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jaroapps.availabledishes.dishes_bottom_nav.data.entity.DishEntity

@Dao
interface DishesDao {
    @Upsert(entity = DishEntity::class)
    suspend fun upsertDish(dishEntity: DishEntity)

    @Query("SELECT * FROM dish_table")
    suspend fun getAllDish(): List<DishEntity>

    @Transaction
    suspend fun changeDishWitsName(dishForDelete: String, newDish: DishEntity) {
        deleteDish(dishForDelete)
        upsertDish(newDish)
    }

    @Query("SELECT * FROM dish_table WHERE name = :name LIMIT 1")
    suspend fun getDishByName(name: String): DishEntity

    @Query("DELETE FROM dish_table WHERE name = :name")
    suspend fun deleteDish(name: String)
}