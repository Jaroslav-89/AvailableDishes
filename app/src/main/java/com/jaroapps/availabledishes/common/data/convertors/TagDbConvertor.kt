package com.jaroapps.availabledishes.common.data.convertors

import com.jaroapps.availabledishes.common.data.db.entity.TagEntity
import com.jaroapps.availabledishes.common.domain.model.Tag

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

    fun mapList(tagList: List<TagEntity>): List<Tag> {
        val result = mutableListOf<Tag>()
        tagList.forEach { tag ->
            result.add(map(tag))
        }
        return result
    }
}