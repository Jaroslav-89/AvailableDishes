package com.jaroapps.availabledishes.common.di

import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.impl.DishesInteractorImpl
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.impl.ProductsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<ProductsInteractor> {
        ProductsInteractorImpl(repository = get())
    }

    single<DishesInteractor> {
        DishesInteractorImpl(repository = get())
    }
}