package com.jaroapps.availabledishes.common.di

import com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.view_model.DetailDishViewModel
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.edit_create_dish.view_model.EditeCreateDishViewModel
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AllDishesViewModel
import com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model.AvailableDishesViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.add_products.view_model.AddProductsViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.view_model.DetailProductViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product.view_model.EditCreateProductViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.view_model.BuyProductsViewModel
import com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.view_model.MyProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AddProductsViewModel(
            productsInteractor = get(),
        )
    }

    viewModel {
        MyProductsViewModel(
            productsInteractor = get(),
        )
    }

    viewModel {
        BuyProductsViewModel(
            productsInteractor = get(),
        )
    }

    viewModel {
        DetailProductViewModel(
            productsInteractor = get(),
        )
    }

    viewModel {
        EditCreateProductViewModel(
            productsInteractor = get(),
        )
    }

    viewModel {
        AllDishesViewModel(
            dishesInteractor = get(),
        )
    }

    viewModel {
        AvailableDishesViewModel(
            dishesInteractor = get(),
        )
    }

    viewModel {
        DetailDishViewModel(
            dishesInteractor = get(),
        )
    }

    viewModel {
        EditeCreateDishViewModel(
            dishesInteractor = get(),
        )
    }
}