package com.task.noteapp.use_case

import com.task.noteapp.commons.di.IoDispatcher
import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.domain.Note
import com.task.noteapp.lib.UseCase
import com.task.store.specification.NoteStore
import kotlinx.coroutines.CoroutineDispatcher
import java.time.LocalDateTime
import javax.inject.Inject

class CreateOrUpdateNoteUseCase @Inject constructor(
    private val noteStore: NoteStore,
    private val logger: Logger,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<CreateOrUpdateNoteUseCase.Params, Note?>(dispatcher, logger) {

    data class Params(
        val noteId: Int?,
        val note: String,
        val title: String?,
        val image: String?,
        val editing: Boolean,
    )

    override suspend fun execute(params: Params): Note? {
        if ((
            params.note.trim().isEmpty() &&
                params.title?.trim().isNullOrBlank() &&
                params.image?.trim().isNullOrBlank()
            ) &&
            params.noteId == null
        ) {
            logger.i("Nothing in the note to save")
            return null
        }

        val noteToSave = params.noteId?.let { id ->
            noteStore.getNote(id)?.copy(
                title = params.title,
                content = params.note,
                image = params.image,
                edited = if (params.editing) LocalDateTime.now() else null,
            )
        } ?: run {
            Note(
                id = 0,
                title = params.title,
                content = params.note,
                image = params.image,
                created = LocalDateTime.now(),
                edited = null,
            )
        }

        return noteStore.save(noteToSave)
    }
}
