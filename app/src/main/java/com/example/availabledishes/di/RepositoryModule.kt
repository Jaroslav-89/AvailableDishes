package com.example.availabledishes.di

import com.example.availabledishes.products_bottom_nav.data.ProductsRepositoryImpl
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductsRepository> {
        ProductsRepositoryImpl(localStorage = get())
    }
}