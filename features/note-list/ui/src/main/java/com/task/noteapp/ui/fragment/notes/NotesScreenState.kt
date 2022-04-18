package com.task.noteapp.ui.fragment.notes

import com.task.noteapp.domain.Note

data class NotesScreenState(
    val isLoading: Boolean,
    val isError: Boolean,
    val notes: List<Note>,
) {

    object Factory {
        val loading = NotesScreenState(
            isLoading = true,
            isError = false,
            notes = emptyList(),
        )
    }
}
