package com.jaroapps.availabledishes.notes_bottom_nav.data.convertors

import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.NoteEntity
import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Note

object NoteDbConvertor {
    fun map(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            name = note.name,
            content = note.content,
            color = note.color,
            author = note.author,
            createData = note.createData,
            lastEditDate = note.lastEditDate,
        )
    }

    fun map(noteEntity: NoteEntity): Note {
        return Note(
            id = noteEntity.id,
            name = noteEntity.name,
            content = noteEntity.content,
            color = noteEntity.color,
            author = noteEntity.author,
            createData = noteEntity.createData,
            lastEditDate = noteEntity.lastEditDate,
        )
    }

    fun mapList(noteEntityList: List<NoteEntity>): List<Note> {
        val noteList = mutableListOf<Note>()
        for (noteEntity in noteEntityList) {
            val note = map(noteEntity)
            noteList.add(note)
        }
        return noteList
    }
}