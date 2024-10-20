package com.jaroapps.availabledishes.notes_bottom_nav.data.convertors

import com.jaroapps.availabledishes.notes_bottom_nav.data.entity.RefrigeratorEntity
import com.jaroapps.availabledishes.notes_bottom_nav.domain.model.Refrigerator

object RefrigeratorDbConvertor {
    fun map(refrigerator: Refrigerator): RefrigeratorEntity {
        return RefrigeratorEntity(
            name = refrigerator.name,
            imgUrl = refrigerator.imgUrl,
            description = refrigerator.description,
            numberOfNotes = refrigerator.numberOfNotes,
            numberOfPersons = refrigerator.numberOfPersons,
            createData = refrigerator.createData,
            lastEditDate = refrigerator.lastEditDate,
        )
    }

    fun map(refrigeratorEntity: RefrigeratorEntity): Refrigerator {
        return Refrigerator(
            name = refrigeratorEntity.name,
            imgUrl = refrigeratorEntity.imgUrl,
            description = refrigeratorEntity.description,
            numberOfNotes = refrigeratorEntity.numberOfNotes,
            numberOfPersons = refrigeratorEntity.numberOfPersons,
            createData = refrigeratorEntity.createData,
            lastEditDate = refrigeratorEntity.lastEditDate,
        )
    }

    fun mapList(refrigeratorsList: List<Refrigerator>): List<RefrigeratorEntity> {
        val refrigeratorsEntityList = mutableListOf<RefrigeratorEntity>()
        for (refrigerator in refrigeratorsList) {
            val refrigeratorEntity = map(refrigerator)
            refrigeratorsEntityList.add(refrigeratorEntity)
        }
        return refrigeratorsEntityList
    }

    fun mapList(refrigeratorsEntityList: List<RefrigeratorEntity>): List<Refrigerator> {
        val refrigeratorsList = mutableListOf<Refrigerator>()
        for (refrigeratorEntity in refrigeratorsEntityList) {
            val refrigerator = map(refrigeratorEntity)
            refrigeratorsList.add(refrigerator)
        }
        return refrigeratorsList
    }
}