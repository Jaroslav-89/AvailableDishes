package com.jaroapps.availabledishes.notes_bottom_nav.ui.edit_create_refrigerator.view_model

import androidx.lifecycle.ViewModel
import com.jaroapps.availabledishes.notes_bottom_nav.domain.api.NotesInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditCreateRefrigeratorViewModel @Inject constructor(
    private val notesInteractor: NotesInteractor
) : ViewModel() {


}