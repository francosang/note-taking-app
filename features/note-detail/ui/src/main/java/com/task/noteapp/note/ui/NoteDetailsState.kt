package com.task.noteapp.note.ui

import java.time.LocalDateTime

data class NoteDetailsState(
    val isClosing: Boolean,
    val isLoading: Boolean,
    val hasErrorLoading: Boolean,
    val hasErrorDeleting: Boolean,
    val hasErrorSaving: Boolean,
    val isEditing: Boolean,
    val noteId: Int?,
    val title: String?,
    val note: String,
    val image: String?,
    val created: LocalDateTime? = null,
    val edited: LocalDateTime? = null,
) {

    object Factory {
        val empty = NoteDetailsState(
            title = null,
            note = "",
            image = null,
            isClosing = false,
            isLoading = false,
            hasErrorLoading = false,
            hasErrorSaving = false,
            hasErrorDeleting = false,
            noteId = null,
            isEditing = false,
        )
    }
}
