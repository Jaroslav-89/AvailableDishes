package com.jaroapps.availabledishes.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table")
data class TagEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val color: String,
    val type: String,
)