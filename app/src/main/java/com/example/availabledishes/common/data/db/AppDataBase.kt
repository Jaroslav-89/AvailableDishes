package com.example.availabledishes.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.availabledishes.common.data.db.entity.TagEntity
import com.example.availabledishes.dishes_bottom_nav.data.entity.DishEntity
import com.example.availabledishes.products_bottom_nav.data.entity.ProductEntity

@Database(
    version = 1,
    entities = [
        ProductEntity::class,
        DishEntity::class,
        TagEntity::class,
    ]
)
abstract class AppDataBase : RoomDatabase() {
}