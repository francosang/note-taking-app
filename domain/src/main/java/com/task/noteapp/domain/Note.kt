package com.task.noteapp.domain

import java.time.LocalDateTime

data class Note(
    val id: Int?,
    val title: String?,
    val content: String,
    val image: String?,
    val created: LocalDateTime,
    val edited: LocalDateTime?,
)
