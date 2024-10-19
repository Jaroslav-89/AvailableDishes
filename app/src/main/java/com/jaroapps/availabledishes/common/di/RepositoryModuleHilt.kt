package com.jaroapps.availabledishes.common.di

import com.jaroapps.availabledishes.common.data.convertors.DishDbConvertor
import com.jaroapps.availabledishes.common.data.convertors.ProductDbConvertor
import com.jaroapps.availabledishes.common.data.db.AppDataBase
import com.jaroapps.availabledishes.dishes_bottom_nav.data.DishesRepositoryImpl
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.jaroapps.availabledishes.notes_bottom_nav.data.NotesRepositoryImpl
import com.jaroapps.availabledishes.notes_bottom_nav.domain.api.NotesRepository
import com.jaroapps.availabledishes.products_bottom_nav.data.ProductsRepositoryImpl
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleHilt {

    @Provides
    @Singleton
    fun provideProductsRepository(
        dataBase: AppDataBase,
    ): ProductsRepository =
        ProductsRepositoryImpl(
            dataBase,
        )

    @Provides
    @Singleton
    fun provideDishesRepository(
        dataBase: AppDataBase,
    ): DishesRepository =
        DishesRepositoryImpl(
            dataBase,
        )

    @Provides
    @Singleton
    fun provideNotesRepository(
        dataBase: AppDataBase,
    ): NotesRepository =
        NotesRepositoryImpl(
            dataBase,
        )
}