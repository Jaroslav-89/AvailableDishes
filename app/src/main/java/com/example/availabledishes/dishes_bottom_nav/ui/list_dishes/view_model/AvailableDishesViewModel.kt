package com.example.availabledishes.dishes_bottom_nav.ui.list_dishes.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish

class AvailableDishesViewModel(
    application: Application,
    private val dishesInteractor: DishesInteractor
) : AndroidViewModel(application) {

    private val _state = MutableLiveData<List<Dish>>()
    val state: LiveData<List<Dish>>
        get() = _state

    fun getAvailableDishes() {
        renderState(dishesInteractor.getAvailableDishes())
    }

    fun toggleFavorite(dish: Dish) {
        dishesInteractor.toggleFavorite(dish)
        renderState(dishesInteractor.getAvailableDishes())
    }

    private fun renderState(dishesList: List<Dish>) {
        _state.postValue(dishesList.sortedBy { it.name.lowercase() }
            .sortedByDescending { it.inFavorite })
    }
}