package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.repository.ObjectsRepository
import com.saal.androidtest.domain.repository.RelationsRepository

class GetObjectWithRelationsUseCase(
    private val objectRepository: ObjectsRepository,
    private val relationsRepository: RelationsRepository
) {

    suspend operator fun invoke(objectId: Long): UseCaseResult<Pair<ObjectModel, List<ObjectModel>>> =
        try {
            val objectModel = objectRepository.getObjectById(objectId)
            if (objectModel == null) {
                UseCaseResult.Error(UseCaseErrors.ObjectNotFound)
            } else {
                val relations = relationsRepository.getRelationsForObject(objectId)
                val relatedObjectIds = mutableListOf<Long>()
                relations.forEach { relation ->
                    if (relation.fromObject != objectId) {
                        relatedObjectIds.add(relation.fromObject)
                    }
                    if (relation.toObject != objectId) {
                        relatedObjectIds.add(relation.toObject)
                    }
                }
                val relatedObjects = objectRepository.getObjectsByIds(relatedObjectIds)
                UseCaseResult.Success(Pair(objectModel, relatedObjects))
            }
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
}