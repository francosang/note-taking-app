package com.task.noteapp.note.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.task.noteapp.ui.navigation.Destination

const val NotePath = "note"
const val NoteArgument = "note-id"

object NoteViewDestination : Destination(
    route = "$NotePath/{$NoteArgument}",
    arguments = listOf(navArgument(NoteArgument) { type = NavType.IntType })
)

object NoteCreateDestination : Destination(NotePath) {
    fun create(noteId: Int) = "$NotePath/$noteId"
}