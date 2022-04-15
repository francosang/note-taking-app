package com.task.noteapp.ui.fragment.notes.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.task.noteapp.ui.fragment.notes.NotesScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.noteListNavGraphRoute(
    onNoteTapped: (noteId: Int) -> Unit,
    onAddNote: () -> Unit,
) {
    composable(route = NotesListDestination.route) {
        // TODO: add navigation
        NotesScreen(
            onNoteTapped = onNoteTapped,
            onAddNote = onAddNote,
        )
    }
}
