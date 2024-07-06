package com.saal.androidtest.domain.use_case

import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.repository.ObjectsRepository

class GetAllObjectsUseCase(private val objectRepository: ObjectsRepository) {

    suspend operator fun invoke(): UseCaseResult<List<ObjectModel>> {
        return try {
            UseCaseResult.Success(objectRepository.getAllObjects())
        } catch (e: Exception) {
            UseCaseResult.Error(UseCaseErrors.GeneralError(e.message))
        }
    }
}