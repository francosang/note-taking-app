package com.task.noteapp.commons.test

import com.task.noteapp.domain.Note
import java.time.LocalDateTime

object NoteMocks {
    val empty = Note(
        id = null,
        title = null,
        content = "",
        image = null,
        created = LocalDateTime.now(),
        edited = null,
    )
}