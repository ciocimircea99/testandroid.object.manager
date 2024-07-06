package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.model.RelationModel
import com.saal.androidtest.domain.repository.ObjectsRepository
import com.saal.androidtest.domain.repository.RelationsRepository

class AddObjectUseCase(
    private val objectRepository: ObjectsRepository,
    private val relationsRepository: RelationsRepository
) {

    suspend operator fun invoke(objectModel: ObjectModel, relatedObjects:List<ObjectModel>): UseCaseResult<Unit> {
        return try {
            val insertedId = objectRepository.addObject(objectModel)
            relatedObjects.forEach { relatedObject ->
                relationsRepository.addRelation(RelationModel(
                    fromObject = insertedId,
                    toObject = relatedObject.id,
                ))
            }
            UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
    }
}