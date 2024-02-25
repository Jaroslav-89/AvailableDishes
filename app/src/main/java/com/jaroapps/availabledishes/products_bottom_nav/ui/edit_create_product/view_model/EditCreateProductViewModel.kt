package com.jaroapps.availabledishes.products_bottom_nav.ui.edit_create_product.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.products_bottom_nav.domain.api.ProductsInteractor
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.launch

class EditCreateProductViewModel(
    private val productsInteractor: ProductsInteractor
) : ViewModel() {

    private var productBeforeChange: Product? = null
    private var productAfterChange: Product? = null

    private val _state = MutableLiveData<Product>()
    private val _productTags = MutableLiveData<List<Tag>>()
    private val _allTags = MutableLiveData<List<Tag>>()
    private val _nameAvailable = MutableLiveData<Boolean>()
    val state: LiveData<Product>
        get() = _state
    val productTags: LiveData<List<Tag>>
        get() = _productTags
    val allTags: LiveData<List<Tag>>
        get() = _allTags
    val nameAvailable: LiveData<Boolean>
        get() = _nameAvailable

    fun getProductByName(productName: String) {
        viewModelScope.launch {
            if (productBeforeChange == null) {
                productBeforeChange = productsInteractor.getProductByName(productName)
            }
            if (productAfterChange == null) {
                productAfterChange = productBeforeChange
            }
            productAfterChange?.let {
                renderState(it)
            }
            getProductTags()
        }
    }

    private fun getProductTags() {
        productBeforeChange?.let { product ->
            viewModelScope.launch {
                productsInteractor.getProductTagList(product.tag).collect() {
                    _productTags.postValue(it)
                }
            }
        }
    }

    fun getAvailableProductTags() {
        viewModelScope.launch {
            _allTags.postValue(productsInteractor.getAllProductTags())
        }
    }

    fun prepareNewProduct() {
        if (productBeforeChange == null) {
            productBeforeChange = Product("")
        }
        if (productAfterChange == null) {
            productAfterChange = Product("")
        }
        productAfterChange?.let {
            renderState(it)
        }
    }

    fun changeProduct() {
        viewModelScope.launch {
            productBeforeChange?.let { productBeforeChange ->
                productAfterChange?.let { productAfterChange ->
                    productsInteractor.changeProduct(productBeforeChange, productAfterChange)
                }
            }
            productBeforeChange = null
            productAfterChange = null
        }
    }

    fun createNewProduct() {
        viewModelScope.launch {
            productAfterChange?.let { productsInteractor.createNewProduct(it) }
            productBeforeChange = null
            productAfterChange = null
        }
    }

    fun editPlaceHolderImg(img: String) {
        productAfterChange = productAfterChange?.copy(imgUrl = img)
        productAfterChange?.let { renderState(it) }
    }

    fun deleteProductImg() {
        productAfterChange = productAfterChange?.copy(imgUrl = "")
        productAfterChange?.let { renderState(it) }
    }

    fun editNameText(text: String) {
        productAfterChange = productAfterChange?.copy(name = text)
        productAfterChange?.let { renderState(it) }
    }

    fun toggleTag(clickTag: Tag, delete: Boolean) {
        var newTagsList = mutableListOf<String>()
        productAfterChange?.let { afterChange ->
            if (!delete) {
                if (afterChange.tag.isEmpty()) {
                    newTagsList.add(clickTag.name)
                } else {
                    newTagsList = afterChange.tag.toMutableList()
                    if (!afterChange.tag.contains(clickTag.name)) {
                        newTagsList.add(clickTag.name)
                    } else {
                        newTagsList = afterChange.tag.toMutableList()
                    }
                }
            } else {
                newTagsList = afterChange.tag.toMutableList()
                newTagsList.remove(clickTag.name)
            }
            productAfterChange = afterChange.copy(tag = newTagsList)
            productAfterChange?.let { renderState(it) }
        }
    }

    fun editDescriptionText(text: String) {
        productAfterChange?.let {
            productAfterChange = it.copy(description = text)
        }
        productAfterChange?.let { renderState(it) }
    }

    fun toggleFavorite() {
        productAfterChange?.let {
            productAfterChange = if (it.inFavorite) {
                it.copy(inFavorite = false, needToBuy = false)
            } else {
                it.copy(inFavorite = true)
            }
        }
        productAfterChange?.let { productAfterChange -> renderState(productAfterChange) }
    }

    fun toggleNeedToBuy() {
        productAfterChange?.let {
            productAfterChange = if (it.needToBuy) {
                it.copy(needToBuy = false)
            } else {
                it.copy(inFavorite = true, needToBuy = true)
            }
        }
        productAfterChange?.let { productAfterChange -> renderState(productAfterChange) }
    }

    fun deleteProduct() {
        viewModelScope.launch {
            productBeforeChange?.let { productsInteractor.deleteProduct(it) }
        }
    }

    fun checkingNameNewProductForMatches() {
        productBeforeChange?.let { beforeChange ->
            productAfterChange?.let { afterChange ->
                if (
                    beforeChange.name.lowercase().trim() == afterChange.name.lowercase().trim()
                ) {
                    renderCheckNameResult(true)
                } else {
                    viewModelScope.launch {
                        renderCheckNameResult(
                            productsInteractor.checkingNameNewProductForMatches(afterChange.name)
                        )
                    }
                }
            }
        }
    }

    private fun renderState(product: Product) {
        _state.postValue(product)
    }

    private fun renderCheckNameResult(nameAvailable: Boolean) {
        _nameAvailable.postValue(nameAvailable)
    }
}