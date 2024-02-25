package com.jaroapps.availabledishes.common.di

import com.jaroapps.availabledishes.dishes_bottom_nav.data.DishesRepositoryImpl
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesRepository
import com.jaroapps.availabledishes.products_bottom_nav.data.ProductsRepositoryImpl
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductsRepository> {
        ProductsRepositoryImpl(
            dataBase = get(),
            productDbConvertor = get(),
            dishDbConvertor = get(),
            tagDbConvertor = get(),
        )
    }

    single<DishesRepository> {
        DishesRepositoryImpl(
            dataBase = get(),
            productDbConvertor = get(),
            dishDbConvertor = get(),
            tagDbConvertor = get(),
        )
    }
}