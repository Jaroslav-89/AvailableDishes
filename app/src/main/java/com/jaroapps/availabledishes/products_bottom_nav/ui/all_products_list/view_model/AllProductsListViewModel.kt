package com.jaroapps.availabledishes.products_bottom_nav.ui.all_products_list.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList
import javax.inject.Inject

class AllProductsListViewModel @Inject constructor(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<ProductList>>()
    val state: LiveData<List<ProductList>>
        get() = _state


}