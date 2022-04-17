package com.task.noteapp.use_case

import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.domain.Note
import com.task.noteapp.lib.ObservableUseCase
import com.task.store.specification.NoteStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllNotesUseCase @Inject constructor(
    private val noteStore: NoteStore,
    logger: Logger,
) : ObservableUseCase<Unit, List<Note>>(logger) {

    override fun createObservable(params: Unit): Flow<List<Note>> {
        return noteStore.observeNotes()
    }
}
