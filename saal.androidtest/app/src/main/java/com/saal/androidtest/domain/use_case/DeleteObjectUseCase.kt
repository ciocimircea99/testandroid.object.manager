package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.repository.ObjectsRepository
import com.saal.androidtest.domain.repository.RelationsRepository

class DeleteObjectUseCase(
    private val objectRepository: ObjectsRepository,
    private val relationsRepository: RelationsRepository
) {

    suspend operator fun invoke(objectModel: ObjectModel): UseCaseResult<Unit> {
        return try {
            relationsRepository.getRelationsForObject(objectModel.id).forEach { relation ->
                relationsRepository.deleteRelation(relation)
            }
            UseCaseResult.Success(objectRepository.deleteObject(objectModel))
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
    }
}