package com.task.noteapp.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.task.noteapp.note.navigation.NoteCreateDestination
import com.task.noteapp.note.navigation.noteDetailNavGraphRoute
import com.task.noteapp.ui.fragment.notes.navigation.NotesListDestination
import com.task.noteapp.ui.fragment.notes.navigation.noteListNavGraphRoute

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val background = MaterialTheme.colors.background

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = background,
            darkIcons = useDarkIcons
        )
    }

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
