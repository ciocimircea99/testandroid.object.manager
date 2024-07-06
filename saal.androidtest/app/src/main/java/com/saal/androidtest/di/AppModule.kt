package com.saal.androidtest.di

import com.saal.androidtest.data.local.AppDatabase
import com.saal.androidtest.data.repository.ObjectsRepositoryImpl
import com.saal.androidtest.data.repository.RelationsRepositoryImpl
import com.saal.androidtest.domain.repository.ObjectsRepository
import com.saal.androidtest.domain.repository.RelationsRepository
import com.saal.androidtest.domain.use_case.AddObjectUseCase
import com.saal.androidtest.domain.use_case.AddRelationUseCase
import com.saal.androidtest.domain.use_case.DeleteObjectUseCase
import com.saal.androidtest.domain.use_case.DeleteRelationUseCase
import com.saal.androidtest.domain.use_case.GetAllObjectsUseCase
import com.saal.androidtest.domain.use_case.GetObjectWithRelationsUseCase
import com.saal.androidtest.domain.use_case.UpdateObjectUseCase
import com.saal.androidtest.ui.viewmodel.AddObjectViewModel
import com.saal.androidtest.ui.viewmodel.EditObjectViewModel
import com.saal.androidtest.ui.viewmodel.ObjectListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getDatabase(get()).objectDao() }
    single { AppDatabase.getDatabase(get()).relationDao() }

    single<ObjectsRepository> { ObjectsRepositoryImpl(get()) }
    single<RelationsRepository> { RelationsRepositoryImpl(get()) }

    factory { AddObjectUseCase(get(),get()) }
    factory { AddRelationUseCase(get()) }
    factory { DeleteObjectUseCase(get(),get()) }
    factory { DeleteRelationUseCase(get()) }
    factory { GetAllObjectsUseCase(get()) }
    factory { GetObjectWithRelationsUseCase(get(),get()) }
    factory { UpdateObjectUseCase(get()) }

    viewModel { ObjectListViewModel(get(),get(),get()) }
    viewModel { AddObjectViewModel(get(),get()) }
    viewModel { EditObjectViewModel(get(),get(),get(),get(),get(),) }
}