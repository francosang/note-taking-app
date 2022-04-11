package com.task.store.specification

import com.task.noteapp.domain.Note

interface NoteStore {
    suspend fun getNotes(): List<Note>
}
