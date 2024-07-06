package com.saal.androidtest.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.saal.androidtest.AndroidTestApplication
import com.saal.androidtest.R
import com.saal.androidtest.domain.UseCaseErrors
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.util.android.SingleLiveEvent

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val errorMessage = SingleLiveEvent<String>()

    val command = SingleLiveEvent<Command>()

    protected fun getErrorMessage(result: UseCaseResult.Error): String =
        with(getApplication<AndroidTestApplication>()) {
            return when (result.error) {
                is UseCaseErrors.AddRelationSameObject -> {
                    getString(R.string.error_add_relation_same_object)
                }

                is UseCaseErrors.AddRelationAlreadyExists -> {
                    getString(R.string.error_add_relation_exists)
                }

                is UseCaseErrors.ObjectNotFound -> {
                    getString(R.string.error_object_not_found)
                }

                is UseCaseErrors.GeneralError -> {
                    result.error.message ?: getString(R.string.error_unknown)
                }
            }
        }

    open class Command
}