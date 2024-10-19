package com.jaroapps.availabledishes.notes_bottom_nav.ui.my_refrigerators.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.availabledishes.notes_bottom_nav.domain.api.NotesInteractor
import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Refrigerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRefrigeratorsViewModel @Inject constructor(
    private val notesInteractor: NotesInteractor
) : ViewModel() {

    private val _state = MutableLiveData<List<Refrigerator>>()
    val state: LiveData<List<Refrigerator>>
        get() = _state

    fun getMyRefrigeratorsList() {
//        viewModelScope.launch {
//            notesInteractor.getMyRefrigeratorsList().collect() {
//                renderState(it)
//            }
//        }
    }
}