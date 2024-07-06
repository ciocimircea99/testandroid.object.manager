package com.saal.androidtest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.use_case.AddRelationUseCase
import com.saal.androidtest.domain.use_case.DeleteRelationUseCase
import com.saal.androidtest.domain.use_case.GetObjectWithRelationsUseCase
import com.saal.androidtest.domain.use_case.UpdateObjectUseCase
import com.saal.androidtest.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditObjectViewModel(
    application: Application,
    private val getObjectWithRelationsUseCase: GetObjectWithRelationsUseCase,
    private val updateObjectUseCase: UpdateObjectUseCase,
    private val addRelationUseCase: AddRelationUseCase,
    private val deleteRelationUseCase: DeleteRelationUseCase
) : BaseViewModel(application) {

    val objectModel: LiveData<ObjectModel>
        get() = _objectModel
    private val _objectModel: MutableLiveData<ObjectModel> = MutableLiveData()

    val relatedObjects: LiveData<List<ObjectModel>>
        get() = _relatedObjects
    private val _relatedObjects: MutableLiveData<List<ObjectModel>> = MutableLiveData(listOf())

    fun initObjectToEdit(objectModel: ObjectModel) {
        if (_objectModel.value == null) {
            getObjectWithRelations(objectModel)
        }
    }

    fun getObjectWithRelations(objectModel: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getObjectWithRelationsUseCase(objectModel.id)) {
                is UseCaseResult.Success -> {
                    _objectModel.postValue(result.data.first)
                    _relatedObjects.postValue(result.data.second)
                }

                is UseCaseResult.Error -> {
                    errorMessage.postValue(getErrorMessage(result))
                }
            }
        }
    }

    fun saveEditedObjectState(name: String, description: String, type: String) {
        val editedObject = _objectModel.value ?: return
        editedObject.apply {
            this.name = name
            this.description = description
            this.type = type
        }
        _objectModel.postValue(editedObject)
    }

    fun addRelation(relatedObject: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _objectModel.value?.let { objectModel ->
                when (val result = addRelationUseCase(objectModel.id, relatedObject.id)) {
                    is UseCaseResult.Success -> {
                        val relations = getObjectWithRelationsUseCase(objectModel.id)
                        if (relations is UseCaseResult.Success) {
                            _relatedObjects.postValue(relations.data.second)
                        }
                    }

                    is UseCaseResult.Error -> {
                        errorMessage.postValue(getErrorMessage(result))
                    }
                }
            }
        }
    }

    fun deleteRelation(relatedObject: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _objectModel.value?.let { objectModel ->
                when (val result = deleteRelationUseCase(objectModel.id, relatedObject.id)) {
                    is UseCaseResult.Success -> {
                        val relations = getObjectWithRelationsUseCase(objectModel.id)
                        if (relations is UseCaseResult.Success) {
                            _relatedObjects.postValue(relations.data.second)
                        }
                    }

                    is UseCaseResult.Error -> {
                        errorMessage.postValue(getErrorMessage(result))
                    }
                }
            }
        }
    }

    fun updateObject(name: String, description: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _objectModel.value?.let { objectModel ->
                objectModel.apply {
                    this.name = name
                    this.description = description
                    this.type = type
                }
                when (val result = updateObjectUseCase(objectModel)) {
                    is UseCaseResult.Success -> {
                        command.postValue(Command.Finish)
                    }

                    is UseCaseResult.Error -> {
                        errorMessage.postValue(getErrorMessage(result))
                    }
                }
            }
        }
    }

    sealed class Command : BaseViewModel.Command() {
        data object Finish : Command()
    }
}