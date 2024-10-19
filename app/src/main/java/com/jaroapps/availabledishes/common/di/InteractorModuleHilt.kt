package com.jaroapps.availabledishes.common.di

import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.impl.DishesInteractorImpl
import com.jaroapps.availabledishes.notes_bottom_nav.domain.api.NotesInteractor
import com.jaroapps.availabledishes.notes_bottom_nav.domain.api.NotesRepository
import com.jaroapps.availabledishes.notes_bottom_nav.domain.impl.NotesInteractorImpl
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import com.jaroapps.availabledishes.products_bottom_nav.domain.impl.ProductsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InteractorModuleHilt {

    @Provides
    @Singleton
    fun provideProductsInteractor(
        repository: ProductsRepository
    ): ProductsInteractor =
        ProductsInteractorImpl(
            repository
        )

    @Provides
    @Singleton
    fun provideDishesInteractor(
        repository: DishesRepository
    ): DishesInteractor =
        DishesInteractorImpl(
            repository
        )

    @Provides
    @Singleton
    fun provideNotesInteractor(
        repository: NotesRepository
    ): NotesInteractor =
        NotesInteractorImpl(
            repository
        )
}
