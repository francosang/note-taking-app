package com.task.noteapp.ui.fragment.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.parcelable.toParcelables
import com.task.noteapp.use_case.GetAllNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
) : ViewModel() {

    var state by mutableStateOf(NotesScreenState.Factory.loading)
        private set

    init {
        initialLoad()
    }

    fun retry() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            loadNotes()
        }
    }

    private fun initialLoad() {
        viewModelScope.launch {
            loadNotes()
        }
    }

    private suspend fun loadNotes() {
        val result = getAllNotesUseCase(Unit)

        state = result.fold(
            onSuccess = { page ->
                state.copy(
                    isLoading = false,
                    notes = page.toParcelables(),
                    isError = false,
                )
            },
            onFailure = {
                state.copy(isError = true)
            }
        )
    }
}
