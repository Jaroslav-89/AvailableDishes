package com.example.availabledishes.products_bottom_nav.ui.my_products.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class BuyProductsViewModel(
    application: Application,
    private val productsInteractor: ProductsInteractor
) : AndroidViewModel(application) {

    private val _state = MutableLiveData<List<Product>>()
    val state: LiveData<List<Product>>
        get() = _state

    fun getBuyProductsList() {
        renderState(productsInteractor.getBuyProductsList())
    }
    fun toggleFavorite(product: Product) {
        productsInteractor.toggleFavorite(product)
        renderState(productsInteractor.getBuyProductsList())
    }

    fun toggleBuy(product: Product) {
        productsInteractor.toggleBuy(product)
        renderState(productsInteractor.getBuyProductsList())
    }

    private fun renderState(productsList: List<Product>) {
        _state.postValue(productsList.sortedBy { it.name })
    }


}