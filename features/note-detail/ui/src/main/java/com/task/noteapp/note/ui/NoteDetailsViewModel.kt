package com.task.noteapp.note.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.note.navigation.NoteArgument
import com.task.noteapp.use_case.CreateOrUpdateNoteUseCase
import com.task.noteapp.use_case.DeleteNoteUseCase
import com.task.noteapp.use_case.GetNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val logger: Logger,
    private val createOrUpdateNoteUseCase: CreateOrUpdateNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(NoteDetailsState.Factory.empty)
        private set

    init {
        val noteId =
            savedStateHandle.get<Int?>(NoteArgument)
        loadNote(noteId)
    }

    fun updateTitle(title: String) {
        state = state.copy(title = title)

        val params = CreateOrUpdateNoteUseCase.Params(
            noteId = state.noteId,
            note = state.note,
            title = title,
            image = state.image
        )

        save(params)
    }

    fun updateContent(note: String) {
        val params = CreateOrUpdateNoteUseCase.Params(
            noteId = state.noteId,
            note = note,
            title = state.title,
            image = state.image
        )

        save(params)
    }

    fun delete() = viewModelScope.launch {
        val result = state.noteId?.let {
            deleteNoteUseCase(it)
        }

        result?.fold(
            onSuccess = {
                state = state.copy(isClosing = true)
            },
            onFailure = {
                state = state.copy(isError = true)
            }
        )
    }

    fun exit() {
        state = state.copy(isClosing = true)
    }

    private fun loadNote(noteId: Int?) = viewModelScope.launch {
        if (noteId != null) {
            state = state.copy(isLoading = true)

            val result = getNoteUseCase(noteId)
            result.onSuccess {
                it?.let { note ->
                    state = state.copy(
                        isLoading = false,
                        noteId = note.id,
                        title = note.title,
                        note = note.content,
                        image = note.image,
                        created = note.created,
                        edited = note.edited,
                    )
                }
            }
            result.onFailure {
                state = state.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    private fun save(params: CreateOrUpdateNoteUseCase.Params) = viewModelScope.launch {
        val result = createOrUpdateNoteUseCase(params)

        result.fold(
            onFailure = {
                logger.e(it)
                state = state.copy(
                    isError = true,
                )
            },
            onSuccess = { note ->
                note?.let {
                    state = state.copy(
                        isError = false,
                        noteId = it.id,
                        note = it.content,
                        title = it.title,
                        image = it.image,
                        created = it.created,
                        edited = it.edited,
                    )
                }
            }
        )
    }
}
