package com.example.availabledishes.products_bottom_nav.ui.my_products.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class MyProductsViewModel(
    application: Application,
    private val productsInteractor: ProductsInteractor
) : AndroidViewModel(application) {

    private val _state = MutableLiveData<List<Product>>()
    val state: LiveData<List<Product>>
        get() = _state

    fun getMyProductsList() {
        renderState(productsInteractor.getMyProductsList())
    }
    fun toggleFavorite(product: Product) {
        productsInteractor.toggleFavorite(product)
        renderState(productsInteractor.getMyProductsList())
    }
    fun toggleBuy(product: Product) {
        productsInteractor.toggleBuy(product)
        renderState(productsInteractor.getMyProductsList())
    }

    private fun renderState(productsList: List<Product>) {
        _state.postValue(productsList.sortedBy { it.name.lowercase() }
            .sortedByDescending { it.needToBuy == false ||  it.needToBuy == null})
    }
}