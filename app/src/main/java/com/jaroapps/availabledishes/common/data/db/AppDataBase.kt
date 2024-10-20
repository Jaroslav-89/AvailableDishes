package com.jaroapps.availabledishes.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jaroapps.availabledishes.common.data.db.dao.DishesDao
import com.jaroapps.availabledishes.common.data.db.dao.ProductsDao
import com.jaroapps.availabledishes.common.data.db.dao.TagDao
import com.jaroapps.availabledishes.common.data.db.entity.TagEntity
import com.jaroapps.availabledishes.dishes_bottom_nav.data.entity.DishEntity
import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.NoteEntity
import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.RefrigeratorEntity
import com.jaroapps.availabledishes.products_bottom_nav.data.entity.ProductEntity

@Database(
    version = 101,
    entities = [
        ProductEntity::class,
        DishEntity::class,
        TagEntity::class,
        RefrigeratorEntity::class,
        NoteEntity::class,
    ]
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun productDao(): ProductsDao
    abstract fun dishDao(): DishesDao
    abstract fun tagDao(): TagDao
}