package com.jaroapps.availabledishes.notes_bottom_nav.domain.model

data class Note (
    val id: String,
    val name: String = "",
    val content: String = "",
    val color: String = "#00FFFFFF",
    val author: String = "",
    val createData: Long,
    val lastEditDate: Long,
)