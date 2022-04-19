package com.task.noteapp.ui.fragment.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.use_case.GetAllNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
) : ViewModel() {

    val state: StateFlow<NotesScreenState> = getAllNotesUseCase.flow.map { notes ->
        notes.fold(
            onSuccess = {
                NotesScreenState(
                    isLoading = false,
                    notes = it,
                    isError = false,
                )
            },
            onFailure = {
                NotesScreenState(
                    isLoading = false,
                    notes = emptyList(),
                    isError = true,
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = NotesScreenState.Factory.loading,
    )

    init {
        refresh()
    }

    fun retry() {
        refresh()
    }

    private fun refresh() {
        getAllNotesUseCase(Unit)
    }
}
