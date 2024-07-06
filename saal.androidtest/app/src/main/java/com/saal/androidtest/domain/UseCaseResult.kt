package com.saal.androidtest.domain

sealed class UseCaseResult<out T> {
    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(val error: UseCaseErrors) : UseCaseResult<Nothing>()
}