package com.jaroapps.availabledishes.notes_bottom_nav.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "refrigerator_table")
data class RefrigeratorEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val imgUrl: String = "",
    val description: String = "",
    val numberOfNotes: Int = 0,
    val numberOfPersons: Int = 0,
    val createData: Long,
    val lastEditDate: Long,
)