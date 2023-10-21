package com.example.availabledishes.dishes_bottom_nav.ui.detail_dish.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.availabledishes.dishes_bottom_nav.domain.api.DishesInteractor
import com.example.availabledishes.dishes_bottom_nav.domain.model.Dish
import com.example.availabledishes.products_bottom_nav.domain.model.Product

class DetailDishViewModel(
    application: Application,
    private val dishesInteractor: DishesInteractor
) : AndroidViewModel(application) {

    private var lastDish: Dish? = null

    private val _state = MutableLiveData<Dish>()
    val state: LiveData<Dish>
        get() = _state

    fun getDishByName(dishName: String) {
        lastDish = dishesInteractor.getDishByName(dishName)
        renderState(dishesInteractor.getDishByName(dishName))
    }

    fun toggleFavorite(dish: Dish) {
        dishesInteractor.toggleFavorite(dish)
        renderState(dishesInteractor.getDishByName(dish.name))
    }

    fun toggleFavoriteProduct(product: Product) {
        dishesInteractor.toggleFavoriteProduct(product)
        renderState(lastDish!!)
    }

    fun toggleBuyProduct(product: Product) {
        dishesInteractor.toggleBuyProduct(product)
        renderState(lastDish!!)
    }

    private fun renderState(dish: Dish) {
        _state.postValue(dish)
    }
}