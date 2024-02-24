package com.example.availabledishes.common.data.convertors

import com.example.availabledishes.common.data.db.entity.TagEntity
import com.example.availabledishes.common.domain.model.Tag

class TagDbConvertor {
    fun map(tag: Tag): TagEntity {
        return TagEntity(
            name = tag.name,
            color = tag.color,
            type = tag.type,
        )
    }

    fun map(tag: TagEntity): Tag {
        return Tag(
            name = tag.name,
            color = tag.color,
            type = tag.type,
        )
    }
}