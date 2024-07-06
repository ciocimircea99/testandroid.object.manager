package com.saal.androidtest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saal.androidtest.domain.UseCaseResult
import com.saal.androidtest.domain.model.ObjectModel
import com.saal.androidtest.domain.use_case.DeleteObjectUseCase
import com.saal.androidtest.domain.use_case.GetAllObjectsUseCase
import com.saal.androidtest.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ObjectListViewModel(
    application: Application,
    private val getAllObjectsUseCase: GetAllObjectsUseCase,
    private val deleteObjectUseCase: DeleteObjectUseCase
) : BaseViewModel(application) {

    val objectList: LiveData<List<ObjectModel>>
        get() = _objectList
    private val _objectList: MutableLiveData<List<ObjectModel>> = MutableLiveData()

    private var unfilteredList: List<ObjectModel> = listOf()

    fun getAllObjects() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getAllObjectsUseCase()) {
                is UseCaseResult.Success -> {
                    unfilteredList = result.data
                    _objectList.postValue(unfilteredList)
                }

                is UseCaseResult.Error -> {
                    errorMessage.postValue(getErrorMessage(result))
                }
            }
        }
    }

    fun deleteObject(objectModel: ObjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = deleteObjectUseCase(objectModel)) {
                is UseCaseResult.Success -> {
                    getAllObjects()
                }

                is UseCaseResult.Error -> {
                    errorMessage.postValue(getErrorMessage(result))
                }
            }
        }
    }

    fun filterEntries(text: String) {
        if (text.isEmpty()) {
            _objectList.postValue(unfilteredList)
        } else {
            val filteredList = unfilteredList.toMutableList()
                .filter { objectModel -> objectModel.toString().contains(text) }
            _objectList.postValue(filteredList)
        }
    }
}