package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model

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
class ProductFragmentViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<ProductFragmentState>(ProductFragmentState.Loading)
    val state: LiveData<ProductFragmentState>
        get() = _state

    fun getProductListInfo(listId: String) {
        viewModelScope.launch {
            val productListInfo = productsInteractor.getProductListById(listId)
            renderState(productListInfo)

        }
    }

    private fun renderState(productListInfo: ProductList) {
        _state.postValue(
            ProductFragmentState.Content(
                productListInfo,
            )
        )
    }
}