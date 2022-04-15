package com.task.noteapp.use_case

import com.task.noteapp.commons.di.IoDispatcher
import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.lib.UseCase
import com.task.store.specification.NoteStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteStore: NoteStore,
    logger: Logger,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Int, Unit>(dispatcher, logger) {

    override suspend fun execute(params: Int) {
        return noteStore.deleteNote(params)
    }
}
