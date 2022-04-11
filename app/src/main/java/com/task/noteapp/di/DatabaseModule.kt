package com.task.noteapp.di

import android.content.Context
import androidx.room.Room
import com.task.store.AppDatabase
import com.task.store.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    private val databaseName = "noteapp-database"

    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
            .build()
    }

    @Provides
    fun providesNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.getNoteDao()
    }
}
