package com.jaroapps.availabledishes.products_bottom_nav.ui.detail_product.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.launch

class DetailProductViewModel(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private var thisProduct: Product? = null

    private val _state = MutableLiveData<Product>()
    val state: LiveData<Product>
        get() = _state

    private val _tagList = MutableLiveData<List<Tag>>()
    val tagList: LiveData<List<Tag>>
        get() = _tagList

    private val _dishesListWithProduct = MutableLiveData<List<Dish>>()
    val dishesListWithProduct: LiveData<List<Dish>>
        get() = _dishesListWithProduct

    fun getProductByName(productName: String) {
        viewModelScope.launch {
            thisProduct = productsInteractor.getProductByName(productName)
            getProductTags()
            getAllDishesWithThisProduct()
            thisProduct?.let { product ->
                renderState(product)
            }
        }
    }

    private fun getProductTags() {
        viewModelScope.launch {
            thisProduct?.let { product ->
                productsInteractor.getProductTagList(product.tag).collect() {
                    _tagList.postValue(it)
                }
            }
        }
    }

    private fun getAllDishesWithThisProduct() {
        viewModelScope.launch {
            thisProduct?.let { product ->
                _dishesListWithProduct.postValue(
                    productsInteractor.getAllDishesWithThisProduct(
                        product
                    )
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            thisProduct?.let { product ->
                productsInteractor.toggleFavorite(product)
                renderState(productsInteractor.getProductByName(product.name))
            }
        }
    }

    fun toggleNeedToBuy() {
        viewModelScope.launch {
            thisProduct?.let { product ->
                productsInteractor.toggleBuy(product)
                renderState(productsInteractor.getProductByName(product.name))
            }
        }
    }

    fun toggleDishFavorite(dish: Dish) {
        viewModelScope.launch {
            productsInteractor.toggleDishFavorite(dish)
            getAllDishesWithThisProduct()
        }
    }

    private fun renderState(product: Product) {
        _state.postValue(product)
    }
}