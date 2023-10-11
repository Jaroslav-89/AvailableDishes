package com.example.availabledishes.products_bottom_nav.ui.create_product.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class CreateProductViewModel(
    application: Application,
    private val productsInteractor: ProductsInteractor
) : AndroidViewModel(application) {

    private val _state = MutableLiveData<Product>()
    val state: LiveData<Product>
        get() = _state

    fun getProductByName(productName: String) {
        renderState(productsInteractor.getProductByName(productName))
    }

    fun toggleFavorite(product: Product) {
        productsInteractor.toggleFavorite(product)
        renderState(productsInteractor.getProductByName(product.name))
    }

    fun createNewProduct(product: Product) {
        productsInteractor.createNewProduct(product)
    }

    fun changeProduct(product: Product, changeProduct: Product) {
        productsInteractor.changeProduct(product, changeProduct)
    }

    private fun renderState(product: Product) {
        _state.postValue(product)
    }

}