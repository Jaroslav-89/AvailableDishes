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
    private val _nameAvailable = MutableLiveData<Boolean>()
    val state: LiveData<Product>
        get() = _state
    val nameAvailable: LiveData<Boolean>
        get() = _nameAvailable

    fun getProductByName(productName: String) {
        if (productBeforeChange == null) {
            productBeforeChange = productsInteractor.getProductByName(productName)
        }
        if (productAfterChange == null) {
            productAfterChange = productBeforeChange
        }
        renderState(productAfterChange!!)
    }

    fun prepareNewProduct() {
        if (productAfterChange == null) {
            productAfterChange = Product("", null, listOf<ProductTag>(), null, null, null)
        }
        renderState(productAfterChange!!)
    }

    fun changeProduct() {
        productsInteractor.changeProduct(productBeforeChange!!, productAfterChange!!)
        productBeforeChange = null
        productAfterChange = null
    }

    fun createNewProduct() {
        productsInteractor.createNewProduct(productAfterChange!!)
        productBeforeChange = null
        productAfterChange = null
    }

    fun editPlaceHolderImg(img: String) {
            productAfterChange = productAfterChange?.copy(imgUrl = img)
            renderState(productAfterChange!!)
    }

    fun deleteProductImg(){
        productAfterChange = productAfterChange?.copy(imgUrl = null)
        renderState(productAfterChange!!)
    }

    fun editNameText(text: String) {
        productAfterChange = productAfterChange?.copy(name = text)
        renderState(productAfterChange!!)
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

    fun editDescriptionText(text: String) {
        productAfterChange = productAfterChange?.copy(description = text)
        renderState(productAfterChange!!)
    }

    fun toggleFavorite() {
        if (productAfterChange?.inFavorite == true) {
            productAfterChange = productAfterChange!!.copy(inFavorite = false)
            productAfterChange = productAfterChange!!.copy(needToBuy = false)
            renderState(productAfterChange!!)
        } else {
            productAfterChange = productAfterChange!!.copy(inFavorite = true)
            renderState(productAfterChange!!)
        }
    }

    fun toggleNeedToBuy() {
        if (productAfterChange?.needToBuy == true) {
            productAfterChange = productAfterChange!!.copy(needToBuy = false)
            renderState(productAfterChange!!)
        } else {
            productAfterChange = productAfterChange!!.copy(inFavorite = true)
            productAfterChange = productAfterChange!!.copy(needToBuy = true)
            renderState(productAfterChange!!)
        }
    }

    fun deleteProduct() {
        productBeforeChange?.let { productsInteractor.deleteProduct(it) }
    }

    fun checkingNameNewProductForMatches() {
        if (productBeforeChange != null) {
            if (productBeforeChange!!.name.lowercase().trim() == productAfterChange!!.name.lowercase().trim()) {
                checkNameResult(true)
                return
            }
        }

        checkNameResult(productsInteractor.checkingNameNewProductForMatches(productAfterChange!!.name))
    }

    private fun renderState(product: Product) {
        _state.postValue(product)
    }

    private fun checkNameResult(nameAvailable: Boolean) {
        _nameAvailable.postValue(nameAvailable)
    }
}