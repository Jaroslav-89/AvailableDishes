package com.example.availabledishes.products_bottom_nav.ui.edit_create_product.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.example.availabledishes.products_bottom_nav.domain.model.Product
import com.example.availabledishes.products_bottom_nav.domain.model.ProductTag

class EditCreateProductViewModel(
    application: Application,
    private val productsInteractor: ProductsInteractor
) : AndroidViewModel(application) {

    private var productBeforeChange: Product? = null
    private var productAfterChange: Product? = null

    private val _state = MutableLiveData<Product>()
    val state: LiveData<Product>
        get() = _state

    fun getProductByName(productName: String) {
        if (productBeforeChange == null) {
            productBeforeChange = productsInteractor.getProductByName(productName)
        }
        if (productAfterChange == null) {
            productAfterChange = productBeforeChange
        }
        renderState(productAfterChange!!)
    }

    fun toggleFavorite(product: Product) {
        productsInteractor.toggleFavorite(product)
        renderState(productsInteractor.getProductByName(product.name))
    }

    fun toggleTag(clickTag: ProductTag, delete: Boolean) {
        var newTagsList = mutableListOf<ProductTag>()
        if (!delete) {
            if (productAfterChange?.tag.isNullOrEmpty()) {
                newTagsList = mutableListOf(clickTag)
                productAfterChange = productAfterChange?.copy(tag = newTagsList.toList())
                renderState(productAfterChange!!)
            } else {
                newTagsList = productAfterChange?.tag!!.toMutableList()
                for (tag in newTagsList) {
                    if (tag.name == clickTag.name) {
                        newTagsList.remove(tag)
                        newTagsList.add(clickTag)
                        productAfterChange = productAfterChange?.copy(tag = newTagsList.toList())
                        renderState(productAfterChange!!)
                        return
                    }
                }
                newTagsList.add(clickTag)
                productAfterChange = productAfterChange?.copy(tag = newTagsList.toList())
                renderState(productAfterChange!!)
            }
        } else {
            newTagsList = productAfterChange?.tag!!.toMutableList()
            for (tag in newTagsList) {
                if (tag.name == clickTag.name) {
                    newTagsList.remove(clickTag)
                    productAfterChange = productAfterChange?.copy(tag = newTagsList.toList())
                    renderState(productAfterChange!!)
                    return
                }
            }
        }
    }

    fun editNameText(text: String){
        productAfterChange = productAfterChange?.copy(name = text)
        renderState(productAfterChange!!)
    }

    fun editDescriptionText(text: String){
        productAfterChange = productAfterChange?.copy(description = text)
        renderState(productAfterChange!!)
    }

    fun prepareNewProduct() {
        if (productAfterChange == null) {
            productAfterChange = Product("", null, mutableListOf<ProductTag>(), null, null, null)
        }
        renderState(productAfterChange!!)
    }

    fun toggleNeedToBuy(product: Product) {
        productsInteractor.toggleBuy(product)
        renderState(productsInteractor.getProductByName(product.name))
    }

    fun deleteProduct(product: Product) {
        productsInteractor.deleteProduct(product)
    }

    fun createNewProduct(product: Product) {
        productsInteractor.createNewProduct(product)
        productBeforeChange = null
        productAfterChange = null
    }

    fun changeProduct(changedProduct: Product) {
        productsInteractor.changeProduct(productBeforeChange!!, changedProduct)
        productBeforeChange = null
        productAfterChange = null
    }

    fun updateName(name: String) {

    }

    private fun renderState(product: Product) {
        _state.postValue(product)
    }
}