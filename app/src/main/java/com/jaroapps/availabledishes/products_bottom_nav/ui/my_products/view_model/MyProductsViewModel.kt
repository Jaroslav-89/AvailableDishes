package com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.launch

class MyProductsViewModel(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<Product>>()
    val state: LiveData<List<Product>>
        get() = _state

    fun getMyProductsList() {
        viewModelScope.launch {
            renderState(productsInteractor.getMyProductsList())
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            productsInteractor.toggleFavorite(product)
            renderState(productsInteractor.getMyProductsList())
        }
    }

    fun toggleBuy(product: Product) {
        viewModelScope.launch {
            productsInteractor.toggleBuy(product)
            renderState(productsInteractor.getMyProductsList())
        }
    }

    private fun renderState(productsList: List<Product>) {
        _state.postValue(productsList)
    }
}