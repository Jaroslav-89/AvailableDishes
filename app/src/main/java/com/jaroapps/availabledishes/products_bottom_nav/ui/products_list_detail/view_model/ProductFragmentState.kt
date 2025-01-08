package com.jaroapps.availabledishes.products_bottom_nav.ui.products_list_detail.view_model

import com.jaroapps.availabledishes.products_bottom_nav.domain.model.ProductList

sealed interface ProductFragmentState {
    data object Loading : ProductFragmentState
    data class Content(
        val productListInfo: ProductList,
    ) : ProductFragmentState
}
