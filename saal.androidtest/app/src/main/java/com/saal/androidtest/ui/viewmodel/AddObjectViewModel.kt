package com.saal.androidtest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.use_case.AddObjectUseCase
import com.saal.androidtest.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddObjectViewModel(
    application: Application,
    private val addObjectUseCase: AddObjectUseCase,
) : BaseViewModel(application) {

    val objectModel: LiveData<ObjectModel>
        get() = _objectModel
    private val _objectModel: MutableLiveData<ObjectModel> = MutableLiveData(ObjectModel.Empty())

    val relatedObjects: LiveData<List<ObjectModel>>
        get() = _relatedObjects
    private val _relatedObjects: MutableLiveData<List<ObjectModel>> = MutableLiveData(listOf())

    fun addRelatedObject(objectModel: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val relatedObjects = _relatedObjects.value ?: listOf()
            val newRelatedObjects = relatedObjects.toMutableList().apply {
                if(!contains(objectModel)) add(objectModel)
            }
            _relatedObjects.postValue(newRelatedObjects)
        }
    }

    fun removeRelatedObject(objectModel: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val relatedObjects = _relatedObjects.value ?: listOf()
            val newRelatedObjects = relatedObjects.toMutableList().apply {
                remove(objectModel)
            }
            _relatedObjects.postValue(newRelatedObjects)
        }
    }

    fun addObject(objectModel: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val relatedObject = _relatedObjects.value ?: listOf()
            when (val result = addObjectUseCase(objectModel, relatedObject)) {
                is UseCaseResult.Success -> {
                    _relatedObjects.postValue(listOf())
                    command.postValue(Command.Finish)
                }

                is UseCaseResult.Error -> {
                    errorMessage.postValue(getErrorMessage(result))
                }
            }
        }
    }

    sealed class Command : BaseViewModel.Command() {
        data object Finish : Command()
    }
}