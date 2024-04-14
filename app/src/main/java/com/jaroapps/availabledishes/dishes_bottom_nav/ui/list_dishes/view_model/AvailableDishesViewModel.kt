package com.jaroapps.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.jaroapps.availabledishes.dishes_bottom_nav.domain.model.Dish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableDishesViewModel @Inject constructor(
    private val dishesInteractor: DishesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<Dish>>()
    val state: LiveData<List<Dish>>
        get() = _state

    fun getAvailableDishes() {
        viewModelScope.launch {
            renderState(dishesInteractor.getAvailableDishes())
        }
    }

    fun toggleFavorite(dish: Dish) {
        viewModelScope.launch {
            dishesInteractor.toggleFavorite(dish)
            renderState(dishesInteractor.getAvailableDishes())
        }
    }

    private fun renderState(dishesList: List<Dish>) {
        _state.postValue(dishesList.sortedBy { it.name.lowercase() }
            .sortedByDescending { it.inFavorite })
    }
}