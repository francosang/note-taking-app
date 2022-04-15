package com.task.noteapp.note.ui

import java.time.LocalDateTime

data class NoteDetailsState(
    val isClosing: Boolean,
    val isLoading: Boolean,
    val isError: Boolean,
    val noteId: Int?,
    val title: String?,
    val note: String,
    val image: String?,
    val created: LocalDateTime? = null,
    val edited: LocalDateTime? = null,
) {

    object Factory {
        val empty = NoteDetailsState(
            title = "",
            note = "",
            image = "",
            isClosing = false,
            isLoading = false,
            isError = false,
            noteId = null,
        )
    }
}
