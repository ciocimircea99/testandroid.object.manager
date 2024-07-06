package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.repository.ObjectsRepository

class UpdateObjectUseCase(private val objectRepository: ObjectsRepository) {

    suspend operator fun invoke(
        objectModel: ObjectModel
    ): UseCaseResult<Unit> {
        return try {
            UseCaseResult.Success(objectRepository.updateObject(objectModel))
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
    }
}