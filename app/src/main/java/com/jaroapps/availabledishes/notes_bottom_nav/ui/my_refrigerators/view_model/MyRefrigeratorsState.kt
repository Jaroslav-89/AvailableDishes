package com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.view_model

import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Refrigerator

sealed interface MyRefrigeratorsState {
    data object Loading : MyRefrigeratorsState
    data class Content(
        val refrigerators: List<Refrigerator>
    ) : MyRefrigeratorsState
}