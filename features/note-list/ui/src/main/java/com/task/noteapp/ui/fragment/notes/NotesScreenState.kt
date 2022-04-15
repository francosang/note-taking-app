package com.task.noteapp.ui.fragment.notes

import com.task.noteapp.parcelable.NoteParcelable

data class NotesScreenState(
    val isLoading: Boolean,
    val isError: Boolean,
    val notes: List<NoteParcelable>,
) {

    object Factory {
        val loading = NotesScreenState(
            isLoading = true,
            isError = false,
            notes = emptyList(),
        )
    }
}
