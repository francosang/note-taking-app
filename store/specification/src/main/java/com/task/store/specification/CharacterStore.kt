package com.task.store.specification

import com.task.noteapp.domain.Note

interface NoteStore {
    suspend fun getNotes(): List<Note>
    suspend fun save(note: Note): Note
    suspend fun getNote(id: Int): Note?
    suspend fun deleteNote(id: Int)
}
