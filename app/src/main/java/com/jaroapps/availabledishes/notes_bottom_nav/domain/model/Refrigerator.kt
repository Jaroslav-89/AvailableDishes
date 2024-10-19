package com.jaroapps.availabledishes.notes_bottom_nav.domain.model

data class Refrigerator(
    val name: String,
    val imgUrl: String = "",
    val description: String = "",
    val numberOfNotes: Int = 0,
    val numberOfPersons: Int = 1,
    val createData: Long,
    val lastEditDate: Long,
)