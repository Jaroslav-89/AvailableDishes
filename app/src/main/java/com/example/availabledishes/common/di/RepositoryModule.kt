package com.example.availabledishes.common.di

import com.example.availabledishes.dishes_bottom_nav.data.DishesRepositoryImpl
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.example.availabledishes.products_bottom_nav.data.ProductsRepositoryImpl
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductsRepository> {
        ProductsRepositoryImpl(localStorage = get())
    }

    single<DishesRepository> {
        DishesRepositoryImpl(localStorage = get())
    }
}