package com.example.availabledishes.products_bottom_nav.ui.detail_product.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class DetailProductViewModel(
    application: Application,
    private val productsInteractor: ProductsInteractor
) : AndroidViewModel(application) {

    private var thisProduct: Product? = null

    private val _state = MutableLiveData<Product>()
    val state: LiveData<Product>
        get() = _state

    private val _dishesListWithProduct = MutableLiveData<List<Dish>>()
    val dishesListWithProduct: LiveData<List<Dish>>
        get() = _dishesListWithProduct

    fun getProductByName(productName: String) {
        thisProduct = productsInteractor.getProductByName(productName)
        renderState(thisProduct!!)
    }

    fun getAllDishesWithThisProduct() {
        _dishesListWithProduct.postValue(productsInteractor.getAllDishesWithThisProduct(thisProduct!!))
    }

    fun toggleFavorite(product: Product) {
        productsInteractor.toggleFavorite(product)
        renderState(productsInteractor.getProductByName(product.name))
    }

    fun toggleNeedToBuy(product: Product) {
        productsInteractor.toggleBuy(product)
        renderState(productsInteractor.getProductByName(product.name))
    }

    fun toggleDishFavorite(dish: Dish){
        productsInteractor.toggleDishFavorite(dish)
    }

    private fun renderState(product: Product) {
        _state.postValue(product)
    }
}