package com.saal.androidtest.domain.repository

import com.saal.androidtest.data.local.entity.RelationEntity
import com.saal.androidtest.domain.model.RelationModel

interface RelationsRepository {

    suspend fun getAllRelations(): List<RelationModel>

    suspend fun getRelationsForObject(objectId: Long): List<RelationModel>

    suspend fun addRelation(relationModel: RelationModel): Long

    suspend fun updateRelation(relationModel: RelationModel)

    suspend fun deleteRelation(relationModel: RelationModel)
}