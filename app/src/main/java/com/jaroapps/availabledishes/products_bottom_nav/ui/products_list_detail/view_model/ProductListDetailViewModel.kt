package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListDetailViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<ProductListDetailState>(ProductListDetailState.Loading)
    val state: LiveData<ProductListDetailState>
        get() = _state

    fun getProductsInList(listId: String) {
        viewModelScope.launch {
            val productListInfo = productsInteractor.getProductListById(listId)
            productsInteractor.getProductsInList(listId).collect() {
                renderState(it, productListInfo)
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            productsInteractor.toggleFavorite(product)
        }
    }

    fun toggleBuy(product: Product) {
        viewModelScope.launch {
            productsInteractor.toggleBuy(product)
        }
    }

    private fun renderState(productsList: List<Product>, productListInfo: ProductList) {
        _state.postValue(
            ProductListDetailState.Content(
                productsList.sortedBy { it.name.lowercase() }
                    .sortedBy { it.needToBuy })
        )
    }
}