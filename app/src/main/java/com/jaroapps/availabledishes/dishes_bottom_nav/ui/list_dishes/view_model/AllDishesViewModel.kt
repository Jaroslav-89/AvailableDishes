package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import kotlinx.coroutines.launch

class AllDishesViewModel(
    private val dishesInteractor: DishesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<Dish>>()
    val state: LiveData<List<Dish>>
        get() = _state

    fun getAllDishes() {
        viewModelScope.launch {
            renderState(dishesInteractor.getAllDishes())
        }
    }

    fun toggleFavorite(dish: Dish) {
        viewModelScope.launch {
            dishesInteractor.toggleFavorite(dish)
            renderState(dishesInteractor.getAllDishes())
        }
    }

    private fun renderState(dishesList: List<Dish>) {
        _state.postValue(dishesList.sortedBy { it.name.lowercase() }
            .sortedByDescending { it.inFavorite })
    }
}