package com.task.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.task.store.converter.LocalDateTimeConverter
import com.task.store.dao.NoteDao
import com.task.store.entity.NoteEntity

const val DB_VERSION_1 = 1

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = DB_VERSION_1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}
