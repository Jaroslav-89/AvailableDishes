package com.example.availabledishes.my_products.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.availabledishes.my_products.domain.api.ProductsInteractor

class ProductsViewModel(
    application: Application,
    private val productsInteractor: ProductsInteractor
): AndroidViewModel(application) {
}