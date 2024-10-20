package com.jaroapps.availabledishes.notes_bottom_nav.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.NoteEntity
import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.RefrigeratorEntity

@Dao
interface NotesDao {
    @Upsert(entity = NoteEntity::class)
    suspend fun upsertRefrigerator(noteEntity: NoteEntity)

    @Query("SELECT * FROM note_table WHERE name = :name LIMIT 1")
    suspend fun getNoteByName(name: String): RefrigeratorEntity

    @Query("SELECT * FROM note_table")
    suspend fun getAllNote(): List<RefrigeratorEntity>

    @Query("DELETE FROM note_table WHERE name = :name")
    suspend fun deleteNote(name: String)
}