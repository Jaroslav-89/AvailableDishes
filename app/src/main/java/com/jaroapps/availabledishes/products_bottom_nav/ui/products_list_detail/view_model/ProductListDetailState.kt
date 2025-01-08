package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model

import com.jaroapps.availabledishes.products_bottom_nav.domain.model.Product

sealed interface ProductListDetailState {
    data object Loading : ProductListDetailState
    data class Content(
        val productsList: List<Product>
    ) : ProductListDetailState
}
