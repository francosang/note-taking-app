package com.task.store.specification

import com.task.noteapp.domain.Note
import kotlinx.coroutines.flow.Flow

interface NoteStore {
    suspend fun getNotes(): List<Note>
    fun observeNotes(): Flow<List<Note>>
    suspend fun save(note: Note): Note
    suspend fun getNote(id: Int): Note?
    suspend fun deleteNote(id: Int)
}
