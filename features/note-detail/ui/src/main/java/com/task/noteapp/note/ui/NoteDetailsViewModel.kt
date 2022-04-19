package com.task.noteapp.note.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.note.navigation.NoteArgument
import com.task.noteapp.use_case.CreateOrUpdateNoteUseCase
import com.task.noteapp.use_case.DeleteNoteUseCase
import com.task.noteapp.use_case.GetNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
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
            image = state.image,
            editing = state.isEditing,
        )

        save(params)
    }

    fun updateContent(note: String) {
        val params = CreateOrUpdateNoteUseCase.Params(
            noteId = state.noteId,
            note = note,
            title = state.title,
            editing = state.isEditing,
            image = state.image
        )

        save(params)
    }

    fun updateImage(uri: Uri) {
        val params = CreateOrUpdateNoteUseCase.Params(
            noteId = state.noteId,
            note = state.note,
            title = state.title,
            editing = state.isEditing,
            image = uri.toString(),
        )

        save(params)
    }

    fun removeImage() {
        val params = CreateOrUpdateNoteUseCase.Params(
            noteId = state.noteId,
            note = state.note,
            title = state.title,
            editing = state.isEditing,
            image = null,
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
                state = state.copy(hasErrorDeleting = true)
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
                        isEditing = true,
                    )
                }
            }
            result.onFailure {
                state = state.copy(
                    isEditing = true,
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    private fun save(params: CreateOrUpdateNoteUseCase.Params) {
        // TODO this needs to be improved
        // Currently, the screen state will not update until
        // the use case finishes persisting, this means
        // the new typed text will not appear immediately
        // in the input text.
        // This could lead to unexpected behaviour, like missing some
        // chars or the input carer appearing in prevous chars.
        // Some possible fixes:
        // 1. use a debouncer in the inputs to persist only when the user stops typing
        // 2. add a button to persist only when the user presses it
        // 3. launch a coroutine to async execute the uc and update the state immediately
        // This is something I would like to discuss with a team.

//        val result = createOrUpdateNoteUseCase(params)
//
//        result.fold(
//            onFailure = {
//                logger.e(it)
//                state = state.copy(
//                    isError = true,
//                )
//            },
//            onSuccess = { note ->
//                note?.let {
//                    state = state.copy(
//                        isError = false,
//                        noteId = it.id,
//                        note = it.content,
//                        title = it.title,
//                        image = it.image,
//                        created = it.created,
//                        edited = it.edited,
//                    )
//                }
//            }
//        )

        // This is a solution to the saving problem using approach 3.
        // But still needs a debouncer to optimize number of writes
        viewModelScope.launch {
            val result = createOrUpdateNoteUseCase(params)
            result.onSuccess {
                it?.let { note ->
                    state = state.copy(
                        hasErrorSaving = false,
                        noteId = note.id
                    )
                }
            }
            result.onFailure {
                state = state.copy(
                    hasErrorSaving = true
                )
            }
        }

        state = state.copy(
            hasErrorLoading = false,
            noteId = params.noteId,
            note = params.note,
            title = params.title,
            image = params.image,
        )
    }
}
