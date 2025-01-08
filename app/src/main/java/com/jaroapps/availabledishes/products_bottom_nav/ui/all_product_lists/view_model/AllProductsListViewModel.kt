package com.jaroapps.availabledishes.products_bottom_nav.ui.all_product_lists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProductListsViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<ProductList>>()
    val state: LiveData<List<ProductList>>
        get() = _state

    fun getAllProductLists() {
        viewModelScope.launch {
            productsInteractor.getAllProductLists().collect() {
                renderState(it)
            }
        }
    }

    private fun renderState(allProductLists: List<ProductList>) {
        _state.postValue(allProductLists.sortedBy { it.name.lowercase() })
    }
}