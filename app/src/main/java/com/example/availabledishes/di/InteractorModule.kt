package com.example.availabledishes.di

import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.impl.ProductsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<ProductsInteractor> {
        ProductsInteractorImpl(repository = get())
    }
}