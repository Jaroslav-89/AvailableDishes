package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model

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
class MyProductsViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<Product>>()
    val state: LiveData<List<Product>>
        get() = _state

//    init {
//        viewModelScope.launch {
//            for (product in AllProducts.allProducts) {
//                productsInteractor.createNewProduct(product)
//            }
//        }
//    }

    fun getProductsInList(listId: String) {
        viewModelScope.launch {
            productsInteractor.getProductsInList(listId).collect() {
                renderState(it)
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

    private fun renderState(productsList: List<Product>) {
        _state.postValue(productsList.sortedBy { it.name.lowercase() }
            .sortedBy { it.needToBuy })
    }
}