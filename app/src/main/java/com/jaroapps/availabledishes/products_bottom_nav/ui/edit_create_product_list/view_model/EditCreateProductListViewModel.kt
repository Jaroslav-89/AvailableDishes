package com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product_list.view_model

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
class EditCreateProductListViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private var productListBeforeChange: ProductList? = null
    private var productListAfterChange: ProductList? = null

    private val _state = MutableLiveData<ProductList>()
    val state: LiveData<ProductList>
        get() = _state

    init {
        productListAfterChange = ProductList()
    }

    fun getProductListDetail(productListId: String) {
        viewModelScope.launch {
            productListBeforeChange = productsInteractor.getProductListById(productListId)
            productListAfterChange = productListBeforeChange
            productListBeforeChange?.let {
                renderState(it)
            }
        }
    }

    fun changeProductListName(newName: String) {
        productListAfterChange?.let {
            productListAfterChange = it.copy(name = newName)
            renderState(productListAfterChange!!)
        }
    }

    fun changeProductListImg(newImgUrl: String) {
        productListAfterChange?.let {
            productListAfterChange = it.copy(imgUrl = newImgUrl)
            renderState(productListAfterChange!!)
        }
    }

    fun saveProductList() {
        viewModelScope.launch {
            productListAfterChange?.let {
                productsInteractor.editCreateProductList(
                    it.copy(
                        id = System.currentTimeMillis().toString() + it.name
                    )
                )
            }
        }
    }

    fun deleteProductList() {
        viewModelScope.launch {
            productListBeforeChange?.let {
                productsInteractor.deleteProductList(it.id)
            }
        }
    }

    private fun renderState(productList: ProductList) {
        _state.postValue(productList)
    }
}