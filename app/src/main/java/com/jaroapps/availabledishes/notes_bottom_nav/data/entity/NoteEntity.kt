package com.jaroapps.availabledishes.notes_bottom_nav.data.entity

data class NoteEntity (
    val name: String = "",
    val content: String = "",
    val color: String = "#00FFFFFF",
    val author: String = "",
    val createData: Long,
    val lastEditDate: Long,
)