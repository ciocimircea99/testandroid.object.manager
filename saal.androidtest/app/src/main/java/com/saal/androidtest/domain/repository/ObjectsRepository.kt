package com.saal.androidtest.domain.repository

import com.saal.androidtest.domain.model.ObjectModel

interface ObjectsRepository {

    suspend fun getAllObjects(): List<ObjectModel>

    suspend fun getObjectById(id: Long): ObjectModel?

    suspend fun addObject(objectModel: ObjectModel) : Long

    suspend fun updateObject(objectModel: ObjectModel)

    suspend fun deleteObject(objectModel: ObjectModel)

    suspend fun getObjectsByIds(relatedObjectIds: List<Long>): List<ObjectModel>
}