package com.example.availabledishes.common.di

import com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.view_model.DetailDishViewModel
import com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AllDishesViewModel
import com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AvailableDishesViewModel
import com.example.availabledishes.products_bottom_nav.ui.add_products.view_model.AddProductsViewModel
import com.example.availabledishes.products_bottom_nav.ui.detail_product.view_model.DetailProductViewModel
import com.example.availabledishes.products_bottom_nav.ui.edit_create_product.view_model.EditCreateProductViewModel
import com.example.availabledishes.products_bottom_nav.ui.my_products.view_model.BuyProductsViewModel
import com.example.availabledishes.products_bottom_nav.ui.my_products.view_model.MyProductsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AddProductsViewModel(
            application = androidApplication(),
            productsInteractor = get()
        )
    }

    viewModel {
        MyProductsViewModel(
            application = androidApplication(),
            productsInteractor = get()
        )
    }

    viewModel {
        BuyProductsViewModel(
            application = androidApplication(),
            productsInteractor = get()
        )
    }

    viewModel {
        DetailProductViewModel(
            application = androidApplication(),
            productsInteractor = get()
        )
    }

    viewModel {
        EditCreateProductViewModel(
            application = androidApplication(),
            productsInteractor = get()
        )
    }


    viewModel {
        AllDishesViewModel(
            application = androidApplication(),
            dishesInteractor = get()
        )
    }

    viewModel {
        AvailableDishesViewModel(
            application = androidApplication(),
            dishesInteractor = get()
        )
    }

    viewModel {
        DetailDishViewModel(
            application = androidApplication(),
            dishesInteractor = get()
        )
    }
}