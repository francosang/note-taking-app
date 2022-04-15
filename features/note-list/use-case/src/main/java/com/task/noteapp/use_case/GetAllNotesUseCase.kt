package com.task.noteapp.use_case

import com.task.noteapp.commons.di.IoDispatcher
import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.domain.Note
import com.task.noteapp.lib.UseCase
import com.task.store.specification.NoteStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val noteStore: NoteStore,
    @IoDispatcher dispatcher: CoroutineDispatcher,
    logger: Logger,
) : UseCase<Unit, List<Note>>(dispatcher, logger) {

    override suspend fun execute(params: Unit): List<Note> {
         return noteStore.getNotes()
    }
}
