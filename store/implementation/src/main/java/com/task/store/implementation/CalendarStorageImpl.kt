package com.task.store.implementation

import com.task.noteapp.domain.Note
import com.task.store.dao.NoteDao
import com.task.store.entity.toDomains
import com.task.store.specification.NoteStore
import javax.inject.Inject

class NoteStoreImpl @Inject constructor(
    private val noteDao: NoteDao,
) : NoteStore {
    override suspend fun getNotes(): List<Note> {
        return noteDao.selectAll().toDomains()
    }
}
