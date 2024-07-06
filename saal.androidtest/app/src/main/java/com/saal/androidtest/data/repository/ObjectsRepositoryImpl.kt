package com.saal.androidtest.data.repository

import com.saal.androidtest.data.local.dao.ObjectDao
import com.saal.androidtest.data.local.entity.ObjectEntity
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.repository.ObjectsRepository

class ObjectsRepositoryImpl(private val objectDao: ObjectDao) : ObjectsRepository {

    override suspend fun getAllObjects(): List<ObjectModel> =
        objectDao.getAllObjects().map { it.toModel() }

    override suspend fun getObjectById(id: Long): ObjectModel? =
        objectDao.getObjectById(id)?.toModel()

    override suspend fun addObject(objectModel: ObjectModel) : Long =
        objectDao.addObject(objectModel.toEntity())

    override suspend fun updateObject(objectModel: ObjectModel) =
        objectDao.updateObject(objectModel.toEntity())

    override suspend fun deleteObject(objectModel: ObjectModel) =
        objectDao.deleteObject(objectModel.toEntity())

    override suspend fun getObjectsByIds(relatedObjectIds: List<Long>): List<ObjectModel> =
        objectDao.getObjectsByIds(relatedObjectIds)


    private fun ObjectModel.toEntity(): ObjectEntity {
        return ObjectEntity(id, name, description, type)
    }

    private fun ObjectEntity.toModel(): ObjectModel {
        return ObjectModel(id, name, description, type)
    }
}