package com.jaroapps.availabledishes.notes_bottom_nav.data

import com.jaroapps.availabledishes.common.data.db.AppDataBase
import com.jaroapps.availabledishes.notes_bottom_nav.domain.api.NotesRepository

class NotesRepositoryImpl(
    private val dataBase: AppDataBase,
) : NotesRepository {
}