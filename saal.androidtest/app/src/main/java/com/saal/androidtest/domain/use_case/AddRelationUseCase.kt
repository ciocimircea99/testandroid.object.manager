package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.RelationModel
import com.saal.androidtest.domain.repository.RelationsRepository

class AddRelationUseCase(private val relationRepository: RelationsRepository) {

    suspend operator fun invoke(fromObject: Long, toObject: Long): UseCaseResult<Unit> {
        try {
            if (fromObject == toObject) {
                return UseCaseResult.Error(UseCaseErrors.AddRelationSameObject)
            }

            val fromObjectRelationsIds = relationRepository.getRelationsForObject(
                fromObject
            ).map { it.toObject }
            if (fromObjectRelationsIds.contains(toObject)) {
                return UseCaseResult.Error(UseCaseErrors.AddRelationAlreadyExists)
            }
            relationRepository.addRelation(
                RelationModel(
                    fromObject = fromObject,
                    toObject = toObject
                )
            )
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
    }
}