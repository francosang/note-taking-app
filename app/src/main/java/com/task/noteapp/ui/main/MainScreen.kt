package com.task.noteapp.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.task.noteapp.note.navigation.NoteCreateDestination
import com.task.noteapp.note.navigation.noteDetailNavGraphRoute
import com.task.noteapp.ui.fragment.notes.navigation.NotesListDestination
import com.task.noteapp.ui.fragment.notes.navigation.noteListNavGraphRoute
import com.task.noteapp.ui.theme.NoteAppTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    NoteAppTheme {
        val navController = rememberAnimatedNavController()
        AnimatedNavHost(
            navController = navController,
            startDestination = NotesListDestination.route
        ) {
            noteListNavGraphRoute(
                onNoteTapped = { noteId ->
                    navController.navigate(NoteCreateDestination.create(noteId))
                },
                onAddNote = {
                    navController.navigate(NoteCreateDestination.route)
                },
            )
            noteDetailNavGraphRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
