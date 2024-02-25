package com.jaroapps.availabledishes.common.di

import androidx.room.Room
import com.jaroapps.availabledishes.common.data.convertors.DishDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.ProductDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.TagDbConvertor
import com.jaroapps.availabledishes.common.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .build()
    }

    factory {
        ProductDbConvertor()
    }

    factory {
        DishDbConvertor()
    }

    factory {
        TagDbConvertor()
    }

//    factory {
//        Gson()
//    }
//
//    single {
//        androidContext().getSharedPreferences(
//            App.SHARED_PREFERENCES,
//            Context.MODE_PRIVATE
//        )
//    }
//
//    single<LocalStorageProducts> {
//        LocalStorageProductsImpl(
//            sharedPreferences = get(),
//            gson = get()
//        )
//    }
//
//    single<LocalStorageDishes> {
//        LocalStorageDishesImpl(
//            sharedPreferences = get(),
//            gson = get()
//        )
//    }
}