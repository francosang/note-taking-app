package com.task.noteapp.ui.navigation

import androidx.navigation.NamedNavArgument

abstract class Destination(
    val route: String,
    open val arguments: List<NamedNavArgument> = emptyList()
)
