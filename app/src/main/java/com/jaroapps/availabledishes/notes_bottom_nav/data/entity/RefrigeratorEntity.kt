package com.jaroapps.availabledishes.notes_bottom_nav.data.entity

data class RefrigeratorEntity(
    val name: String,
    val imgUrl: String = "",
    val description: String = "",
    val numberOfNotes: Int = 0,
    val numberOfPersons: Int = 0,
    val createData: Long,
    val lastEditDate: Long,
)