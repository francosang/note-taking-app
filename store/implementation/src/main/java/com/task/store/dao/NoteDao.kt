package com.task.store.dao

import androidx.room.Dao
import androidx.room.Query
import com.task.store.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun selectAll(): List<NoteEntity>
}
