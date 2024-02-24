package com.example.availabledishes.common.di

import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.example.availabledishes.dishes_bottom_nav.domain.impl.DishesInteractorImpl
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.impl.ProductsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<ProductsInteractor> {
        ProductsInteractorImpl(repository = get())
    }

    single<DishesInteractor> {
        DishesInteractorImpl(repository = get())
    }
}