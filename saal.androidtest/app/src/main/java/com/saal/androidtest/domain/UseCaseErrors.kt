package com.saal.androidtest.domain

sealed class UseCaseErrors {
    data object AddRelationSameObject : UseCaseErrors()
    data object AddRelationAlreadyExists : UseCaseErrors()
    data object ObjectNotFound : UseCaseErrors()
    data class GeneralError(val message: String?) : UseCaseErrors()
}