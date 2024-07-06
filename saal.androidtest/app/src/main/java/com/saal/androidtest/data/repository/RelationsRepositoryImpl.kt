package com.saal.androidtest.data.repository

import com.saal.androidtest.data.local.dao.RelationDao
import com.saal.androidtest.data.local.entity.RelationEntity
import com.saal.androidtest.domain.model.RelationModel
import com.saal.androidtest.domain.repository.RelationsRepository

class RelationsRepositoryImpl(private val relationDao: RelationDao) : RelationsRepository {
    override suspend fun getAllRelations(): List<RelationModel> =
        relationDao.getAllRelations().map { it.toModel() }

    override suspend fun getRelationsForObject(objectId: Long): List<RelationModel> =
        relationDao.getRelationshipsForObject(objectId).map { it.toModel() }

    override suspend fun addRelation(relationModel: RelationModel): Long =
        relationDao.addRelation(relationModel.toEntity())

    override suspend fun updateRelation(relationModel: RelationModel) =
        relationDao.updateRelation(relationModel.toEntity())

    override suspend fun deleteRelation(relationModel: RelationModel) =
        relationDao.deleteRelation(relationModel.toEntity())

    private fun RelationModel.toEntity(): RelationEntity {
        return RelationEntity(id, fromObject, toObject)
    }

    private fun RelationEntity.toModel(): RelationModel {
        return RelationModel(id, fromObject, toObject)
    }
}