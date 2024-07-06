package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.repository.RelationsRepository

class DeleteRelationUseCase(
    private val relationsRepository: RelationsRepository
) {

    suspend operator fun invoke(fromObject: Long, toObject: Long): UseCaseResult<Unit> {
        return try {
            relationsRepository.getRelationsForObject(
                fromObject
            ).find { it.toObject == toObject }?.let {
                relationsRepository.deleteRelation(it)
            }
            relationsRepository.getRelationsForObject(
                toObject
            ).find { it.toObject == fromObject }?.let {
                relationsRepository.deleteRelation(it)
            }
            UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
    }
}