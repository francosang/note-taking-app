package com.task.noteapp.note.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.task.noteapp.note.ui.NoteDetailsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.noteDetailNavGraphRoute(
    onBack: () -> Unit
) {
    composable(
        route = NoteCreateDestination.route,
        enterTransition = { slideInVertically(tween(700)) { 1800 } },
        exitTransition = { slideOutVertically(tween(700)) { 1800 } },
    ) {
        NoteDetailsScreen(
            popUpScreen = onBack,
        )
    }
    composable(
        route = NoteViewDestination.route,
        arguments = NoteViewDestination.arguments,
    ) {
        NoteDetailsScreen(
            popUpScreen = onBack,
        )
    }
}
