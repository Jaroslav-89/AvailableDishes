package com.jaroapps.availabledishes.dishes_bottom_nav.ui.detail_dish.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.common.domain.model.Tag
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product
import kotlinx.coroutines.launch

class DetailDishViewModel(
    private val dishesInteractor: DishesInteractor
) : ViewModel() {

    private var lastDish: Dish? = null

    private val _state = MutableLiveData<Dish>()
    val state: LiveData<Dish>
        get() = _state

    private val _tagList = MutableLiveData<List<Tag>>()
    val tagList: LiveData<List<Tag>>
        get() = _tagList

    private val _dishIngredients = MutableLiveData<List<Product>>()
    val dishIngredients: LiveData<List<Product>>
        get() = _dishIngredients

    fun getDishByName(dishName: String) {
        viewModelScope.launch {
            lastDish = dishesInteractor.getDishByName(dishName)
            getDishTags()
            getDishIngredients()
            renderState(dishesInteractor.getDishByName(dishName))
        }
    }

    fun getDishTags() {
        viewModelScope.launch {
            lastDish?.let { dish ->
                dishesInteractor.getDishTagList(dish.tag).collect() {
                    _tagList.postValue(it)
                }
            }
        }
    }

    fun getDishIngredients() {
        viewModelScope.launch {
            lastDish?.let { dish ->
                _dishIngredients.postValue(dishesInteractor.getDishIngredients(dish.ingredients))
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            lastDish?.let { dish ->
                dishesInteractor.toggleFavorite(dish)
                renderState(dishesInteractor.getDishByName(dish.name))
            }
        }
    }

    fun toggleFavoriteProduct(product: Product) {
        viewModelScope.launch {
            dishesInteractor.toggleFavoriteProduct(product)
            lastDish?.let { dish ->
                renderState(dishesInteractor.getDishByName(dish.name))
            }
        }
    }

    fun toggleBuyProduct(product: Product) {
        viewModelScope.launch {
            dishesInteractor.toggleBuyProduct(product)
            lastDish?.let { dish ->
                renderState(dishesInteractor.getDishByName(dish.name))
            }
        }
    }

    private fun renderState(dish: Dish) {
        _state.postValue(dish)
    }
}