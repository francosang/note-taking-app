package com.task.noteapp.di

import com.task.store.implementation.NoteStoreImpl
import com.task.store.specification.NoteStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class StoreModuleBinds {

    @Binds
    abstract fun bindNoteStore(impl: NoteStoreImpl): NoteStore
}
