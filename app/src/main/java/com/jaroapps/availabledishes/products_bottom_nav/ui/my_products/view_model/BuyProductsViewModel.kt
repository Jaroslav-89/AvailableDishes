package com.jaroapps.availabledishes.products_bottom_nav.ui.my_products.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyProductsViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<Product>>()
    val state: LiveData<List<Product>>
        get() = _state

    fun getBuyProductsList() {
        viewModelScope.launch {
            renderState(productsInteractor.getBuyProductsList())
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            productsInteractor.toggleFavorite(product)
            renderState(productsInteractor.getBuyProductsList())
        }
    }

    fun toggleBuy(product: Product) {
        viewModelScope.launch {
            productsInteractor.toggleBuy(product)
            renderState(productsInteractor.getBuyProductsList())
        }
    }

    private fun renderState(productsList: List<Product>) {
        _state.postValue(productsList.sortedBy { it.name.lowercase() })
    }
}