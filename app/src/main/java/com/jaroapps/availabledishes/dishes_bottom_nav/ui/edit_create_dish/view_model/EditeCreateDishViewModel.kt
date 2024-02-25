package com.jaroapps.availabledishes.dishes_bottom_nav.ui.edit_create_dish.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.launch

class EditeCreateDishViewModel(
    private val dishesInteractor: DishesInteractor
) : ViewModel() {

    private var dishBeforeChange: Dish? = null
    private var dishAfterChange: Dish? = null

    private val _state = MutableLiveData<Dish>()
    private val _dishTags = MutableLiveData<List<Tag>>()
    private val _allTags = MutableLiveData<List<Tag>>()
    private val _queryProducts = MutableLiveData<List<Product>>()
    private val _nameAvailable = MutableLiveData<Boolean>()
    val state: LiveData<Dish>
        get() = _state
    val productTags: LiveData<List<Tag>>
        get() = _dishTags
    val allTags: LiveData<List<Tag>>
        get() = _allTags
    val queryProducts: LiveData<List<Product>>
        get() = _queryProducts
    val nameAvailable: LiveData<Boolean>
        get() = _nameAvailable

    fun getProductByName(dishName: String) {
        viewModelScope.launch {
            if (dishBeforeChange == null) {
                dishBeforeChange = dishesInteractor.getDishByName(dishName)
            }
            if (dishAfterChange == null) {
                dishAfterChange = dishBeforeChange
            }
            dishAfterChange?.let {
                renderState(it)
            }
            getDishTags()
        }
    }

    private fun getDishTags() {
        dishBeforeChange?.let { dish ->
            viewModelScope.launch {
                dishesInteractor.getDishTagList(dish.tag).collect() {
                    _dishTags.postValue(it)
                }
            }
        }
    }

    fun getAvailableDishTags() {
        viewModelScope.launch {
            _allTags.postValue(dishesInteractor.getAllDishesTags())
        }
    }

    fun getSearchedProducts(query: String) {

    }

    fun prepareNewDish() {
        if (dishBeforeChange == null) {
            dishBeforeChange = Dish("")
        }
        if (dishAfterChange == null) {
            dishAfterChange = Dish("")
        }
        dishAfterChange?.let {
            renderState(it)
        }
    }

    fun changeDish() {
        viewModelScope.launch {
            dishBeforeChange?.let { dishBeforeChange ->
                dishAfterChange?.let { dishAfterChange ->
                    dishesInteractor.changeDish(dishBeforeChange, dishAfterChange)
                }
            }
            dishBeforeChange = null
            dishAfterChange = null
        }
    }

    fun createNewDish() {
        viewModelScope.launch {
            dishAfterChange?.let { dishesInteractor.createNewDish(it) }
            dishBeforeChange = null
            dishAfterChange = null
        }
    }


    fun editPlaceHolderImg(img: String) {
        dishAfterChange = dishAfterChange?.copy(imgUrl = img)
        dishAfterChange?.let { renderState(it) }
    }

    fun deleteDishImg() {
        dishAfterChange = dishAfterChange?.copy(imgUrl = "")
        dishAfterChange?.let { renderState(it) }
    }

    fun editNameText(text: String) {
        dishAfterChange = dishAfterChange?.copy(name = text)
        dishAfterChange?.let { renderState(it) }
    }

    fun toggleTag(clickTag: Tag, delete: Boolean) {
        var newTagsList = mutableListOf<String>()
        dishAfterChange?.let { afterChange ->
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
            dishAfterChange = afterChange.copy(tag = newTagsList)
            dishAfterChange?.let { renderState(it) }
        }
    }

    fun editDescriptionText(text: String) {
        dishAfterChange?.let {
            dishAfterChange = it.copy(description = text)
        }
        dishAfterChange?.let { renderState(it) }
    }

    fun toggleFavorite() {
        dishAfterChange?.let {
            dishAfterChange = if (it.inFavorite) {
                it.copy(inFavorite = false)
            } else {
                it.copy(inFavorite = true)
            }
        }
        dishAfterChange?.let { productAfterChange -> renderState(productAfterChange) }
    }

    fun deleteDish() {
        viewModelScope.launch {
            dishBeforeChange?.let { dishesInteractor.deleteDish(it) }
        }
    }

    fun checkingNameNewDishForMatches() {
        dishBeforeChange?.let { beforeChange ->
            dishAfterChange?.let { afterChange ->
                if (
                    beforeChange.name.lowercase().trim() == afterChange.name.lowercase().trim()
                ) {
                    renderCheckNameResult(true)
                } else {
                    viewModelScope.launch {
                        renderCheckNameResult(
                            dishesInteractor.checkingNameNewDishForMatches(afterChange.name)
                        )
                    }
                }
            }
        }
    }

    private fun renderState(dish: Dish) {
        _state.postValue(dish)
    }

    private fun renderCheckNameResult(nameAvailable: Boolean) {
        _nameAvailable.postValue(nameAvailable)
    }
}